package main

import java.io.File

class Day10: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val (startingPos, grid) = getGridAndPositions(filePath)
        return startingPos.sumOf { explorePaths(grid, it, mutableSetOf()) }
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val (startingPos, grid) = getGridAndPositions(filePath)
        return startingPos.sumOf { explorePaths(grid, it, null) }
    }

    private fun getGridAndPositions(filePath: String): Pair<MutableSet<Point>, List<List<Int>>> {
        val startingPos = mutableSetOf<Point>()
        val grid = File(filePath).readLines().mapIndexed { y, str -> str.mapIndexed { x, c ->
            if (c == '0') startingPos.add(Point(x, y))
            c.digitToInt()
        }}
        return startingPos to grid
    }

    private fun explorePaths(grid: List<List<Int>>, currentPosition: Point, alreadyReachedNines: MutableSet<Point>?): Long {
        val currentHeight = grid[currentPosition.y][currentPosition.x]
        if (currentHeight == 9) {
            if (alreadyReachedNines == null) return 1
            if (alreadyReachedNines.contains(currentPosition)) return 0
            alreadyReachedNines.add(currentPosition)
            return 1
        }

        var result = 0L
        if (currentPosition.y > 0) {
            val up = grid[currentPosition.y - 1][currentPosition.x]
            if (up == currentHeight + 1) {
                result += explorePaths(grid, Point(currentPosition.x, currentPosition.y - 1), alreadyReachedNines)
            }
        }
        if (currentPosition.y < grid.lastIndex) {
            val down = grid[currentPosition.y + 1][currentPosition.x]
            if (down == currentHeight + 1) {
                result += explorePaths(grid, Point(currentPosition.x, currentPosition.y + 1), alreadyReachedNines)
            }
        }
        if (currentPosition.x > 0) {
            val left = grid[currentPosition.y][currentPosition.x - 1]
            if (left == currentHeight + 1) {
                result += explorePaths(grid, Point(currentPosition.x - 1, currentPosition.y), alreadyReachedNines)
            }
        }
        if (currentPosition.x < grid[0].lastIndex) {
            val right = grid[currentPosition.y][currentPosition.x + 1]
            if (right == currentHeight + 1) {
                result += explorePaths(grid, Point(currentPosition.x + 1, currentPosition.y), alreadyReachedNines)
            }
        }
        return result
    }

    private data class Point(val x: Int, val y: Int)
}