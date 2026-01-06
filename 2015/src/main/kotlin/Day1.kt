package main

import java.io.File

class Day1: Day {
    override fun processTextInputPartOne(filePath: String): String {
        var currentFloor = 0

        File(filePath).readText().forEach { if (it == '(') currentFloor++ else currentFloor-- }

        return currentFloor.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        var currentFloor = 0

        File(filePath).readText().forEachIndexed { index, char ->
            if (char == '(') currentFloor++ else currentFloor--
            if (currentFloor < 0) return (index + 1).toString()
        }

        error("Never entered basement")
    }
}