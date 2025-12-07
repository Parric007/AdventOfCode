package main

import java.io.File
import kotlin.math.absoluteValue

class Day1() : Day{

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        val lines = File(filePath).readText().split(Regex("\\s+"))

        val leftValues = ArrayList<Long>(lines.size / 2)
        val rightValues = ArrayList<Long>(lines.size / 2)

        lines.forEachIndexed { index, s ->
            if (index % 2 == 0) leftValues += s.toLong()
            else rightValues += s.toLong()
        }
        leftValues.sort()
        rightValues.sort()

        for (i in leftValues.indices) {
            result += (rightValues[i] - leftValues[i]).absoluteValue
        }

        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val lines = File(filePath).readText().split(Regex("\\s+"))

        val leftValues = ArrayList<Long>(lines.size / 2)
        val rightValues = ArrayList<Long>(lines.size / 2)

        lines.forEachIndexed { index, s ->
            if (index % 2 == 0) leftValues += s.toLong()
            else rightValues += s.toLong()
        }
        leftValues.sort()
        val counts: Map<Long, Int> = rightValues.groupingBy { it }.eachCount()

        for (i in leftValues.indices) {
            val valueOnTheLeft = leftValues[i]
            result += (valueOnTheLeft * (counts[valueOnTheLeft] ?: 0))
        }

        return result
    }
}