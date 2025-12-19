package main

import java.io.File

class Day6 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        return getAllVisitedTiles(filePath).sumOf { subList -> subList.count {it} }.toLong()
    }

    private fun getAllVisitedTiles(filePath: String): List<BooleanArray> {
        val lines = File(filePath).readLines()
        val xDimension = lines[0].length
        val yDimension = lines.size

        val visited = List(yDimension) {
            BooleanArray(xDimension)
        }
        var currentX = 0 // x is index in subarray
        var currentY = 0 // y is index in main array
        var currentDirection = -1 to 0

        for ((i, row) in lines.withIndex()) {
            for ((j, char) in row.withIndex()) {
                if (char == '^') {
                    currentX = j
                    currentY = i
                    visited[currentY][currentX] = true
                }
            }
        }

        while (true) {
            var (nextX, nextY) = currentX + currentDirection.second to currentY + currentDirection.first
            if (nextX !in 0..< xDimension || nextY !in 0..< yDimension) {
                return visited
            }

            while (lines[nextY][nextX] == '#') {
                currentDirection = rotate90Degrees(currentDirection)
                nextX = currentX + currentDirection.second
                nextY = currentY + currentDirection.first
            }
            currentX = nextX
            currentY = nextY
            visited[currentY][currentX] = true
        }
    }

    private fun rotate90Degrees(toRotate: Pair<Int, Int>): Pair<Int, Int> {
        return toRotate.second to -toRotate.first
    }

    private fun getVisitedPositions(filePath: String): List<Pair<Int, Int>> {
        val visitedGrid = getAllVisitedTiles(filePath)
        val result = mutableListOf<Pair<Int, Int>>()

        for (y in visitedGrid.indices) {
            for (x in visitedGrid[y].indices) {
                if (visitedGrid[y][x]) {
                    result += x to y
                }
            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val lines = File(filePath).readLines()
        val height = lines.size
        val width = lines[0].length

        val directions = listOf(
            -1 to 0,
            0 to 1,
            1 to 0,
            0 to -1
        )

        fun dirIndex(dir: Pair<Int, Int>) = directions.indexOf(dir)
        var startX = 0
        var startY = 0
        val startDirection = -1 to 0

        for ((y, row) in lines.withIndex()) {
            for ((x, c) in row.withIndex()) {
                if (c == '^') {
                    startX = x
                    startY = y
                }
            }
        }

        fun createsLoopWithObstacleAt(blockX: Int, blockY: Int): Boolean {
            var x = startX
            var y = startY
            var dir = startDirection

            val visited = Array(height) {
                Array(width) { BooleanArray(4) }
            }
            visited[y][x][dirIndex(dir)] = true

            while (true) {
                var nextX = x + dir.second
                var nextY = y + dir.first
                if (nextX !in 0 until width || nextY !in 0 until height) {
                    return false
                }

                fun isWall(nx: Int, ny: Int): Boolean =
                    lines[ny][nx] == '#' || (nx == blockX && ny == blockY)
                while (isWall(nextX, nextY)) {
                    dir = rotate90Degrees(dir)
                    nextX = x + dir.second
                    nextY = y + dir.first
                }

                x = nextX
                y = nextY

                val d = dirIndex(dir)

                if (visited[y][x][d]) {
                    return true
                }

                visited[y][x][d] = true
            }
        }

        var loopCount = 0
        val candidates = getVisitedPositions(filePath)
        for ((x, y) in candidates) {
            if (x == startX && y == startY) continue
            if (lines[y][x] != '.') continue

            if (createsLoopWithObstacleAt(x, y)) {
                loopCount++
            }
        }

        return loopCount.toLong()
    }


}