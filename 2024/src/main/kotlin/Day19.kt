package main

import java.io.File

class Day19: Day {

    private var towelsCombination = mutableListOf<Long>()

    override fun processTextInputPartOne(filePath: String): Long {
        val (towelsString, designsString) = File(filePath).readText().split("\n\n")

        val towelMap: Map<Char, List<CharArray>> =
            towelsString.split(",")
                .map { it.trim() }
                .groupBy(
                    keySelector = { it[0] },
                    valueTransform = { it.substring(1).toCharArray() }
                )
        val designs = designsString.split("\n").map { it.toCharArray() }

        for (design in designs) {
            towelsCombination.add(solveTowel(towelMap, design))
        }
        return towelsCombination.count { it != 0L }.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return towelsCombination.sum()
    }

    private fun solveTowel(towelMap: Map<Char, List<CharArray>>, design: CharArray): Long {
        val n = design.size
        val dp = LongArray(n + 1)
        dp[n] = 1L

        for (i in n - 1 downTo 0) {
            val options = towelMap[design[i]] ?: continue

            for (towel in options) {
                val end = i + towel.size
                if (end >= n) continue

                var match = true
                for (j in towel.indices) {
                    if (design[i + 1 + j] != towel[j]) {
                        match = false
                        break
                    }
                }

                if (match) {
                    dp[i] += dp[end + 1]
                }
            }
        }

        return dp[0]
    }
}