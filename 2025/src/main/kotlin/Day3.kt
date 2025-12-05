package main

import java.io.File

class Day3: Day {

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            val numberArray = line.chunked(1)
            val indexOfFirstMax = numberArray.indexOf(numberArray.max())
            val firstMax = numberArray[indexOfFirstMax]
            var switchOrder = false
            val secondMax: String
            if (indexOfFirstMax == numberArray.size - 1) {
                secondMax = numberArray.filterIndexed {index, _ ->  index != indexOfFirstMax}.max()
                switchOrder = true
            } else {
                secondMax = numberArray.subList(indexOfFirstMax + 1, numberArray.size).max()
            }

            result += if (switchOrder) (secondMax + firstMax).toLong()
            else (firstMax + secondMax).toLong()
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            val joltage = StringBuilder()
            var start = 0

            while (joltage.length < 12) {
                val end = line.length - (12 -joltage.length)
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

            result += joltage.toString().toLong()
        }
        return result
    }
}