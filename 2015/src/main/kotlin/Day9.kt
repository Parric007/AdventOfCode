package main

import java.io.File

class Day9: Day {
    override fun processTextInputPartOne(filePath: String): String {
        return computeDistance(filePath, true)
    }

    override fun processTextInputPartTwo(filePath: String): String {
        return computeDistance(filePath, false)
    }

    private fun computeDistance(filePath: String, findMin: Boolean): String {
        val citySet = mutableSetOf<String>()
        val edges = mutableListOf<Triple<String, String, Int>>()

        File(filePath).forEachLine { line ->
            val (a, b, c) = line.split(Regex(" to | = "))
            citySet += a
            citySet += b
            edges += Triple(a, b, c.toInt())
        }

        val cities = citySet.toList()
        val n = cities.size
        val index = cities.withIndex().associate { it.value to it.index }

        val dist = Array(n) { IntArray(n) }
        for ((a, b, c) in edges) {
            val i = index[a]!!
            val j = index[b]!!
            dist[i][j] = c
            dist[j][i] = c
        }

        val path = IntArray(n) { it }
        var best = if (findMin) Int.MAX_VALUE else 0

        fun permute(l: Int) {
            if (l == n) {
                var total = 0
                for (i in 0 until n - 1) {
                    total += dist[path[i]][path[i + 1]]
                    if (findMin && total >= best) return
                }
                best = if (findMin) minOf(best, total) else maxOf(best, total)
                return
            }

            for (i in l until n) {
                path[l] = path[i].also { path[i] = path[l] }
                permute(l + 1)
                path[l] = path[i].also { path[i] = path[l] }
            }
        }

        permute(0)
        return best.toString()
    }
}