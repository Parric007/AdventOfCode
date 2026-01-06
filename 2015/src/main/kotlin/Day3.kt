package main

import java.io.File

class Day3: Day {
    override fun processTextInputPartOne(filePath: String): String {
        val houses = mutableSetOf<Position>()
        var currentPosition = Position(0, 0)

        houses.add(currentPosition)

        File(filePath).readText().forEach { char ->
            currentPosition = move(currentPosition, char)
            houses += currentPosition
        }
        return houses.size.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val houses = mutableSetOf<Position>()
        val positions = arrayOf(Position(0, 0), Position(0, 0))

        houses.add(positions[0])

        File(filePath).readText().forEachIndexed { i, char ->
            val index = i % 2
            positions[index] = move(positions[index], char)
            houses += positions[index]
        }
        return houses.size.toString()
    }

    private data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position): Position {
            return Position(x + other.x, y + other.y)
        }
    }

    private fun move(position: Position, c: Char): Position = when (c) {
        '^' -> position + Position(0, -1)
        'v' -> position + Position(0, 1)
        '<' -> position + Position(-1, 0)
        '>' -> position + Position(1, 0)
        else -> position
    }
}