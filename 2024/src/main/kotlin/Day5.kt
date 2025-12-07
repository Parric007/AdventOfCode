package main

import java.io.File

class Day5: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val (unparsedRules, prints) = File(filePath).readText().split("\n\n")

        val pageRules = buildPageRules(unparsedRules)

        return prints.split("\n").map { print: String ->
            val pageNumbersInPrint = print.split(",").map { it.toInt() }
            if (!isLineCorrect(pageRules, pageNumbersInPrint)) return@map 0
            return@map pageNumbersInPrint[pageNumbersInPrint.size / 2]
        }.sum().toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val (unparsedRules, prints) = File(filePath).readText().split("\n\n")
        val pageRules = buildPageRules(unparsedRules)

        return prints.split("\n").mapNotNull { print: String ->
            val pageNumbersInPrint = print.split(",").map { it.toInt() }
            if (!isLineCorrect(pageRules, pageNumbersInPrint)) {
                val fixed = fixLine(pageRules, pageNumbersInPrint)
                return@mapNotNull fixed[fixed.size / 2]
            }
            return@mapNotNull null
        }.sum().toLong()
    }

    private fun buildPageRules(unparsedRules: String): Map<Int, MutableSet<Int>> {
        val pageRules = mutableMapOf<Int, MutableSet<Int>>()
        unparsedRules.split("\n").forEach {  rangeString: String ->
            val (start, end) = rangeString.split("|").map { it.toInt() }
            pageRules.computeIfAbsent(start) { mutableSetOf()}.add(end)
            pageRules.computeIfAbsent(end) { mutableSetOf() }

        }
        return pageRules
    }

    private fun isLineCorrect(rules: Map<Int, Set<Int>>, pages: List<Int>): Boolean {
        val seen = mutableSetOf<Int>()

        for (page in pages) {
            if (rules[page]?.any { it in seen } == true) return false
            seen.add(page)
        }
        return true
    }

    private fun fixLine(rules: Map<Int, Set<Int>>, pages: List<Int>): List<Int> {
        val subNodes = pages.toSet()

        val inDegree = mutableMapOf<Int, Int>().apply {
            for (p in subNodes) put(p, 0)
        }
        for (a in subNodes) {
            for (b in rules[a].orEmpty()) {
                if (b in subNodes) {
                    inDegree[b] = inDegree[b]!! + 1
                }
            }
        }

        val queue = ArrayDeque(inDegree.filterValues { it == 0 }.keys)
        val result = mutableListOf<Int>()

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            result.add(node)
            for (next in rules[node].orEmpty()) {
                if (next in subNodes) {
                    inDegree[next] = inDegree[next]!! - 1
                    if (inDegree[next] == 0) {
                        queue.add(next)
                    }
                }
            }
        }

        return result
    }
}