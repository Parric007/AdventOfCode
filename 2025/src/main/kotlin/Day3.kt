package main

import java.io.File

class Day3: Day {

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            result += calculateJoltage(2, line)
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            result += calculateJoltage(12, line)
        }
        return result
    }

    private fun calculateJoltage(length: Int, line: String): Long {
        val joltage = StringBuilder()
        var start = 0

        while (joltage.length < length) {
            val end = line.length - (length -joltage.length)
            var maxDigit = '0'
            var maxIndex = start

            for (i in start..end) {
                if (line[i] > maxDigit) {
                    maxDigit = line[i]
                    maxIndex = i
                }
            }
            joltage.append(maxDigit)
            start = maxIndex + 1
        }

        return joltage.toString().toLong()
    }
}