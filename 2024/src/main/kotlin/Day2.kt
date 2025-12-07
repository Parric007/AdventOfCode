package main

import java.io.File
import kotlin.math.absoluteValue

class Day2 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            val levels = line.split(" ").map { s: String ->
                s.toLong()
            }
            if (checkIfLineIsFine(levels)) result++
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            val levels = line.split(" ").map { s: String ->
                s.toLong()
            }.toMutableList()

            if (checkIfLineIsFine(levels)) {
                result++
                return@forEachLine
            }

            for (i in levels.indices) {
                val modified = levels.toMutableList().also { it.removeAt(i) }
                if (checkIfLineIsFine(modified)) {
                    result++
                    break
                }
            }
        }

        return result
    }

    private fun checkIfLineIsFine(levels: List<Long>): Boolean {
        var lastLevel = levels[0]
        val isDecreasing = levels[0] > levels[1]

        for (i in 1 until levels.size) {
            val currentLevel = levels[i]
            if (!((currentLevel - lastLevel).absoluteValue in 1..3 &&
                        if (isDecreasing) lastLevel > currentLevel else currentLevel > lastLevel)) {
                return false
            }
            lastLevel = currentLevel
        }
        return true
    }
}