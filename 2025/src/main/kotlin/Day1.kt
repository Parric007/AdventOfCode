package main

import java.io.File
import kotlin.math.absoluteValue

class Day1(){

    fun processTextInputPartOne(filePath: String): String {
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

        return result.toString()
    }

    fun processTextInputPartTwo(filePath: String): String {
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
            val toAdd = difference.absoluteValue / 100

            result += toAdd

            difference %= 100

            var dontCountZero = false

            if (difference < 0 && safePointer + difference <= 0) {
                result++
                dontCountZero = true
            } else if (difference + safePointer >= 100 && difference > 0) {
                result++
                dontCountZero = true
            }

            safePointer = (safePointer + difference).mod(100)

            if (!dontCountZero && safePointer == 0 && toAdd == 0) result++
        }

        return result.toString()
    }
}