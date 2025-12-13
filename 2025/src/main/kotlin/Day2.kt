package main

import java.io.File
import kotlin.math.pow

class Day2 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        val fileLine = File(filePath).readText()

        val ranges = fileLine.split(",").map {
            val (a, b) = it.split("-")
            a.toLong() to b.toLong()
        }

        for ((start, end) in ranges) {
            val minLen = start.toString().length
            val maxLen = end.toString().length
            for (length in minLen..maxLen) {
                if (length % 2 != 0) continue
                val half = length / 2
                val generateCost = 10.0.pow(half.toDouble()).toLong()
                val rangeSize = end - start + 1
                if (rangeSize > generateCost) {
                    for (num in generateRepeatTwice(length)) {
                        if (num in start..end) {
                            result += num
                        }
                    }
                } else {
                    val lower = maxOf(start, 10.0.pow(length - 1).toLong())
                    val upper = minOf(end, 10.0.pow(length).toLong() - 1)
                    for (i in lower..upper) {
                        if (repeatsTwice(i)) {
                            result += i
                        }
                    }
                }
            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val fileLine = File(filePath).readText()

        val ranges = fileLine.split(",").map {
            val (start, end) = it.split("-")
            start.toLong() to end.toLong()
        }

        for ((start, end) in ranges) {
            val minLen = start.toString().length
            val maxLen = end.toString().length

            for (length in minLen..maxLen) {
                for (num in generateRepeatingNumbers(length)) {
                    if (num in start..end) {
                        result += num
                    }
                }
            }
        }
        return result
    }

    private fun repeatsTwice(n: Long): Boolean {
        val s = n.toString()
        if (s.length % 2 != 0) return false
        val half = s.length / 2
        return s.substring(0, half) == s.substring(half)
    }

    private fun generateRepeatTwice(length: Int): Sequence<Long> = sequence {
        val half = length / 2
        val max = 10.0.pow(half.toDouble()).toInt()

        for (pattern in 0 until max) {
            val p = pattern.toString().padStart(half, '0')

            // Final number must not have leading zero
            if (p[0] == '0') continue

            yield((p + p).toLong())
        }
    }

    private fun generateRepeatingNumbers(length: Int): Sequence<Long> = sequence {
        for (patternLength in 1..length / 2) {
            if (length % patternLength != 0) continue

            val maxPattern = 10.0.pow(patternLength.toDouble()).toInt()

            for (pattern in 0 until maxPattern) {
                val patternStr = pattern.toString().padStart(patternLength, '0')

                if (patternStr[0] == '0' || isRepeating(patternStr)) continue

                val repeated = patternStr.repeat(length / patternLength)
                yield(repeated.toLong())
            }
        }
    }

    private fun isRepeating(s: String): Boolean {
        val doubled = s + s
        return doubled.substring(1, doubled.length - 1).contains(s)
    }
}