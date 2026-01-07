package main

import java.io.File

class Day6: Day {

    private val instructionList = mutableListOf<Instruction>()

    override fun processTextInputPartOne(filePath: String): String {
        val grid = BooleanArray(AREA)

        parseInstructions(filePath)

        for ((command, x1, y1, x2, y2) in instructionList) {
            when (command) {
                0 -> { // toggle
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            grid[base] = !grid[base]
                            base++
                        }
                    }
                }

                1 -> { // turn on
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            grid[base] = true
                            base++
                        }
                    }
                }

                else -> { // turn off
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            grid[base] = false
                            base++
                        }
                    }
                }
            }
        }

        var count = 0
        for (v in grid) if (v) count++
        return count.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val grid = IntArray(AREA)

        for ((command, x1, y1, x2, y2) in instructionList) {
            when (command) {
                0 -> { // toggle
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            grid[base] += 2
                            base++
                        }
                    }
                }

                1 -> { // turn on
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            grid[base] += 1
                            base++
                        }
                    }
                }

                else -> { // turn off
                    for (y in y1..y2) {
                        var base = y * SIZE + x1
                        val end = y * SIZE + x2
                        while (base <= end) {
                            val v = grid[base] - 1
                            grid[base] = if (v < 0) 0 else v
                            base++
                        }
                    }
                }
            }
        }

        var total = 0
        for (v in grid) total += v
        return total.toString()
    }

    private data class Instruction(val command: Int, val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    private fun parseInstructions(filePath: String) {
        instructionList.clear()
        File(filePath).forEachLine { line ->
            var i: Int
            val command = when {
                line.startsWith("toggle") -> { i = 7; 0 }
                line.startsWith("turn on") -> { i = 8; 1 }
                else -> { i = 9; 2 }
            }

            var x1 = 0; var y1 = 0
            while (line[i] != ',') x1 = x1 * 10 + (line[i++] - '0')
            i++
            while (line[i].isDigit()) y1 = y1 * 10 + (line[i++] - '0')

            i += 9 // skip " through "

            var x2 = 0; var y2 = 0
            while (line[i] != ',') x2 = x2 * 10 + (line[i++] - '0')
            i++
            while (i < line.length && line[i].isDigit()) y2 = y2 * 10 + (line[i++] - '0')

            instructionList.add(Instruction(command, x1, y1, x2, y2))
        }
    }


    private companion object {
        private const val SIZE = 1000
        private const val AREA = SIZE * SIZE
    }
}