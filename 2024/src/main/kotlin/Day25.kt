package main

import java.io.File

class Day25: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val shapes = File(filePath).readText().split("\n\n").map { it.split("\n") }

        val max = shapes[0].size

        val keys = mutableListOf<IntArray>()
        val locks = mutableListOf<IntArray>()
        shapes.forEach { shape ->
            val converted = convertShapeToNumArray(shape)
            if (shape.first().all { it == '#' }) keys.add(converted)
            else locks.add(converted)
        }

        var result = 0L

        keys.forEach { key ->
            lockForLoop@for (lock in locks) {
                for (i in key.indices) {
                    if (key[i] + lock[i] > max) continue@lockForLoop
                }
                result++
            }
        }

        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return -1L
    }

    private fun convertShapeToNumArray(shape: List<String>): IntArray {
        val toReturn = IntArray(shape[0].length)

        for (i in toReturn.indices) {
            toReturn[i] = shape.count { it[i] == '#' }
        }
        return toReturn
    }
}