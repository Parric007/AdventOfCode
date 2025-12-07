package main

import java.io.File

class Day7 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        val lines = File(filePath).readLines()
        val width = lines[0].length
        var tachyonIndexList = BooleanArray(width)

        tachyonIndexList[lines[0].indexOf('S')] = true

        for (i in 1 until lines.size) {
            val nextIndices = BooleanArray(width)
            val line = lines[i]
            for (index in tachyonIndexList.indices) {
                if (!tachyonIndexList[index]) continue

                if (line[index] == '^') {
                    result++
                    nextIndices[index - 1] = true
                    nextIndices[index + 1] = true
                } else {
                    nextIndices[index] = true
                }
            }
            tachyonIndexList = nextIndices
        }

        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val lines = File(filePath).readLines()
        val width = lines[0].length
        var tachyonPathCounts = LongArray(width)

        tachyonPathCounts[lines[0].indexOf('S')] = 1L

        for (i in 1 until lines.size) {
            val nextIndices = LongArray(width)
            val line = lines[i]
            for (index in tachyonPathCounts.indices) {
                val possiblePathsUpToHere = tachyonPathCounts[index]
                if (tachyonPathCounts[index] == 0L) continue

                if (line[index] == '^') {
                    result++
                    nextIndices[index - 1] += possiblePathsUpToHere
                    nextIndices[index + 1] += possiblePathsUpToHere
                } else {
                    nextIndices[index] += possiblePathsUpToHere
                }
            }
            tachyonPathCounts = nextIndices
        }

        return tachyonPathCounts.sum()
    }
}