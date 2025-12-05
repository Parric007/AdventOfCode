package main

import java.io.File
import kotlin.math.absoluteValue

class Day1() : Day{

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0

        var safePointer = 50

        File(filePath).forEachLine {
            line ->
                safePointer = if (line.startsWith("L")) {
                    (safePointer - line.drop(1).toInt()).mod(100)
                } else {
                    (safePointer + line.drop(1).toInt()).mod(100)
                }
            if (safePointer == 0) {
                result++
            }
        }

        return result.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0
        var safePointer = 50
        var difference: Int

        File(filePath).forEachLine {
                line ->
            difference = if (line.startsWith("L")) {
                -line.drop(1).toInt()
            } else {
                line.drop(1).toInt()
            }

            result += difference.absoluteValue / 100
            var newSafePointer = (safePointer + difference % 100)

            if (safePointer != 0)
            if (newSafePointer < 0) {
                result++
                newSafePointer += 100
            } else if (newSafePointer > 100) {
                result++
                newSafePointer -= 100
            }

            newSafePointer = (newSafePointer + 100000) % 100
            if (newSafePointer == 0) result++

            safePointer = newSafePointer
        }

        return result.toLong()
    }
}