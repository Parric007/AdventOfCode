package main

import java.io.File

class Day6 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val lines = File(filePath).readLines()
        val results = lines[0].trim().split(Regex("\\s+")).map { it.toLong() }.toMutableList()
        val operators = lines[lines.size - 1].split(Regex("\\s+"))

        for (i in 1..<lines.size - 1) {
            lines[i].trim().split(Regex("\\s+")).forEachIndexed { index, string ->
                if (operators[index] == "+") {
                    results[index] += string.toLong()
                } else {
                    results[index] *= string.toLong()
                }
            }
        }
        return results.sum()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val lines = File(filePath).readLines()
        val data = lines.dropLast(1)

        val chunkBoardIndexes = lines.last().mapIndexedNotNull { index, s ->
            if (s != ' ') index else null
        }

        chunkBoardIndexes.zip(chunkBoardIndexes.drop(1) + listOf(lines.maxOf { it.length })).map { (start, end) ->
            val width = end - start
            (0 until width).mapNotNull { dx ->
                val col = start + dx
                val s = buildString {
                    for (row in data) {
                        if (col < row.length && row[col].isDigit()) {
                            append(row[col])
                        }
                    }
                }
                if (s.isNotEmpty()) s.toLong() else null
            }
        }.forEachIndexed { index, numBlock ->
            result += if (lines.last()[chunkBoardIndexes[index]] == '+') numBlock.sum()
            else numBlock.fold(1L) { acc, numBlockMulti -> acc * numBlockMulti }
        }

        return result
    }

}