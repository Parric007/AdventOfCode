package main

import java.io.File

class Day11: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        return blinkNTimes(filePath, 25)
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return blinkNTimes(filePath, 75)
    }

    private fun blinkNTimes(filePath: String, blinkCount: Int): Long {
        val counts = HashMap<Long, Long>(4096)

        val input = File(filePath).readText().trim().split(" ")
        for (s in input) {
            val v = s.toLong()
            counts[v] = (counts[v] ?: 0L) + 1L
        }

        val next = HashMap<Long, Long>(counts.size * 2)
        repeat(blinkCount) {
            next.clear()

            for ((stone, count) in counts) {
                if (stone == 0L) {
                    next[1L] = (next[1L] ?: 0L) + count
                    continue
                }

                val digits = digitCount(stone)

                if ((digits and 1) == 0) {
                    val half = digits shr 1
                    val pow = pow10[half]

                    val left = stone / pow
                    val right = stone - left * pow

                    next[left] = (next[left] ?: 0L) + count
                    next[right] = (next[right] ?: 0L) + count
                } else {
                    val v = stone * 2024L
                    next[v] = (next[v] ?: 0L) + count
                }
            }
            counts.clear()
            counts.putAll(next)
        }

        var total = 0L
        for (c in counts.values) total += c
        return total
    }

    private val pow10 = LongArray(19).apply {
        this[0] = 1L
        for (i in 1 until size) this[i] = this[i - 1] * 10L
    }

    private fun digitCount(n: Long): Int {
        var x = n
        var d = 0
        do {
            d++
            x /= 10
        } while (x != 0L)
        return d
    }
}