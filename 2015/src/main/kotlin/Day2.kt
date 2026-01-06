package main

import java.io.File
import kotlin.math.min

class Day2: Day {
    override fun processTextInputPartOne(filePath: String): String {
        var result = 0
        File(filePath).readLines().forEach { line ->
            val (length, width, height) = line.split("x").map { it.toInt() }

            val lw = length * width
            val wh = width * height
            val lh = length * height

            result += 2 * (lw + wh + lh) + min(min(lw, wh), lh)
        }
        return result.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        var result = 0
        File(filePath).readLines().forEach { line ->
            val (length, width, height) = line.split("x").map { it.toInt() }

            val lw = length + width
            val wh = width + height
            val lh = length + height

            result += width * length * height + min(min(lw, wh), lh) * 2
        }
        return result.toString()
    }
}