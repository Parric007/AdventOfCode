package main

import java.io.File

class Day12: Day {
    private val listOfFields: MutableList<Field> = mutableListOf()

    override fun processTextInputPartOne(filePath: String): Long {
        val garden = File(filePath).readLines()
        listOfFields.clear()
        var currentStartingPosition = Point(0, 0)
        val visitedMap = mutableSetOf<Point>()
        bigWhile@while (true) {
            while (visitedMap.contains(currentStartingPosition)) {
                currentStartingPosition = currentStartingPosition.inc(garden) ?: break@bigWhile
            }
            val currentChar = getAt(garden, currentStartingPosition)
            val currentField = Field(0, 0, currentChar, 0)
            exploreNeighbors(garden, currentStartingPosition, currentChar, currentField, visitedMap)
            listOfFields.add(currentField)
            currentStartingPosition = currentStartingPosition.inc(garden) ?: break@bigWhile
        }
        return listOfFields.sumOf { it.area * it.perimeter }.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return listOfFields.sumOf { it.area * it.sides }.toLong()
    }

    private fun exploreNeighbors(
        garden: List<String>,
        currentPosition: Point,
        compareChar: Char,
        currentField: Field,
        visited: MutableSet<Point>
    ) {
        if (visited.contains(currentPosition)) return
        visited.add(currentPosition)
        currentField.area++

        val x = currentPosition.x
        val y = currentPosition.y
        val height = garden.size
        val width = garden[0].length

        // UP
        if (y == 0 || garden[y - 1][x] != compareChar) {
            currentField.perimeter++
            if (isStartOfSide(x, y, Dir.UP, garden, compareChar)) {
                currentField.sides++
            }
        } else {
            exploreNeighbors(garden, Point(x, y - 1), compareChar, currentField, visited)
        }

        // DOWN
        if (y == height - 1 || garden[y + 1][x] != compareChar) {
            currentField.perimeter++
            if (isStartOfSide(x, y, Dir.DOWN, garden, compareChar)) {
                currentField.sides++
            }
        } else {
            exploreNeighbors(garden, Point(x, y + 1), compareChar, currentField, visited)
        }

        // LEFT
        if (x == 0 || garden[y][x - 1] != compareChar) {
            currentField.perimeter++
            if (isStartOfSide(x, y, Dir.LEFT, garden, compareChar)) {
                currentField.sides++
            }
        } else {
            exploreNeighbors(garden, Point(x - 1, y), compareChar, currentField, visited)
        }

        // RIGHT
        if (x == width - 1 || garden[y][x + 1] != compareChar) {
            currentField.perimeter++
            if (isStartOfSide(x, y, Dir.RIGHT, garden, compareChar)) {
                currentField.sides++
            }
        } else {
            exploreNeighbors(garden, Point(x + 1, y), compareChar, currentField, visited)
        }
    }



    private fun getAt(list: List<String>, point: Point): Char {
        return list[point.y][point.x]
    }

    private fun isStartOfSide(
        x: Int,
        y: Int,
        dir: Dir,
        grid: List<String>,
        char: Char
    ): Boolean {

        val height = grid.size
        val width = grid[0].length

        fun isBoundary(cx: Int, cy: Int, d: Dir): Boolean {
            return when (d) {
                Dir.UP    -> cy == 0 || grid[cy - 1][cx] != char
                Dir.DOWN  -> cy == height - 1 || grid[cy + 1][cx] != char
                Dir.LEFT  -> cx == 0 || grid[cy][cx - 1] != char
                Dir.RIGHT -> cx == width - 1 || grid[cy][cx + 1] != char
            }
        }

        return when (dir) {
            Dir.UP, Dir.DOWN -> {
                val px = x - 1
                // no previous cell → start
                if (px < 0 || grid[y][px] != char) return true
                // previous cell exists — start only if it does NOT have same boundary
                !isBoundary(px, y, dir)
            }

            Dir.LEFT, Dir.RIGHT -> {
                val py = y - 1
                if (py < 0 || grid[py][x] != char) return true
                !isBoundary(x, py, dir)
            }
        }
    }

    private data class Point(val x: Int, val y: Int) {
        fun inc(grid: List<String>): Point? {
            var newX = x
            var newY = y
            newX++
            if (newX == grid[0].length) {
                newX = 0
                newY++
                if (newY == grid.size)
                    return null
            }
            return Point(newX, newY)
        }
    }

    private data class Field(var area: Int, var perimeter: Int, val char: Char, var sides: Int)

    private enum class Dir {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}