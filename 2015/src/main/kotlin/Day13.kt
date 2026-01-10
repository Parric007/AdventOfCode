package main

import java.io.File
import kotlin.math.max

class Day13: Day {
    override fun processTextInputPartOne(filePath: String): String {
        return solve(filePath, includeSelf = false)
    }

    override fun processTextInputPartTwo(filePath: String): String {
        return solve(filePath, includeSelf = true)
    }

    private fun solve(filePath: String, includeSelf: Boolean): String {
        val tempMap = mutableMapOf<String, MutableMap<String, Int>>()

        File(filePath).forEachLine { line ->
            val p = line.split(" ")
            val from = p[0]
            val to = p.last().dropLast(1)
            val value = if (p[2] == "gain") p[3].toInt() else -p[3].toInt()

            tempMap.computeIfAbsent(from) { mutableMapOf() }[to] = value
        }

        if (includeSelf) {
            tempMap["me"] = mutableMapOf()
            for (k in tempMap.keys) {
                tempMap[k]!!["me"] = 0
                tempMap["me"]!![k] = 0
            }
        }

        val names = tempMap.keys.toList()
        val n = names.size
        val index = names.withIndex().associate { it.value to it.index }

        val pair = Array(n) { IntArray(n) }
        for ((a, map) in tempMap) {
            val i = index[a]!!
            for ((b, v) in map) {
                val j = index[b]!!
                pair[i][j] += v
                pair[j][i] += v
            }
        }

        val fixed = 0
        val perm = IntArray(n - 1) { it + 1 }

        var best = Int.MIN_VALUE

        fun permute(pos: Int) {
            if (pos == perm.size) {
                var sum = 0
                var prev = fixed

                for (x in perm) {
                    sum += pair[prev][x]
                    prev = x
                }

                sum += pair[prev][fixed]
                best = max(best, sum)
                return
            }

            for (i in pos until perm.size) {
                perm[pos] = perm[i].also { perm[i] = perm[pos] }
                permute(pos + 1)
                perm[pos] = perm[i].also { perm[i] = perm[pos] }
            }
        }

        permute(0)
        return best.toString()
    }
}