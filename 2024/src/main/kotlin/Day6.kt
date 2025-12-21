package main

import java.io.File

@Suppress("DuplicatedCode")
class Day6 : Day {
    private val dx = intArrayOf(0, 1, 0, -1)
    private val dy = intArrayOf(-1, 0, 1, 0)

    override fun processTextInputPartOne(filePath: String): Long {
        val lines = File(filePath).readLines()
        val height = lines.size
        val width = lines[0].length

        val visited = Array(height) { BooleanArray(width) }

        var currentX = 0
        var currentY = 0
        var dir = 0
        for (yy in 0 until height) {
            for (xx in 0 until width) {
                if (lines[yy][xx] == '^') {
                    currentX = xx
                    currentY = yy
                }
            }
        }

        visited[currentY][currentX] = true

        while (true) {
            var nx = currentX + dx[dir]
            var ny = currentY + dy[dir]

            if (nx !in 0 until width || ny !in 0 until height) break

            while (lines[ny][nx] == '#') {
                dir = (dir + 1) and 3
                nx = currentX + dx[dir]
                ny = currentY + dy[dir]
            }

            currentX = nx
            currentY = ny
            visited[currentY][currentX] = true
        }

        var count = 0L
        for (row in visited) {
            for (cell in row) {
                if (cell) count++
            }
        }
        return count
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val lines = File(filePath).readLines()
        val height = lines.size
        val width = lines[0].length

        var startX = 0
        var startY = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (lines[y][x] == '^') {
                    startX = x
                    startY = y
                }
            }
        }

        val visitedPositions = mutableListOf<Int>()
        run {
            val visited = Array(height) { BooleanArray(width) }
            var x = startX
            var y = startY
            var dir = 0
            visited[y][x] = true
            visitedPositions.add(y * width + x)

            while (true) {
                var nx = x + dx[dir]
                var ny = y + dy[dir]
                if (nx !in 0 until width || ny !in 0 until height) break

                while (lines[ny][nx] == '#') {
                    dir = (dir + 1) and 3
                    nx = x + dx[dir]
                    ny = y + dy[dir]
                }

                x = nx
                y = ny
                if (!visited[y][x]) {
                    visited[y][x] = true
                    visitedPositions.add(y * width + x)
                }
            }
        }

        val visited = Array(height) { IntArray(width) }
        var loopCount = 0

        fun createsLoop(blockX: Int, blockY: Int): Boolean {
            var x = startX
            var y = startY
            var dir = 0

            for (row in visited) row.fill(0)

            visited[y][x] = 1 shl 0

            while (true) {
                var nx = x + dx[dir]
                var ny = y + dy[dir]

                if (nx !in 0 until width || ny !in 0 until height) return false

                while (lines[ny][nx] == '#' || (nx == blockX && ny == blockY)) {
                    dir = (dir + 1) and 3
                    nx = x + dx[dir]
                    ny = y + dy[dir]
                }

                x = nx
                y = ny

                val bit = 1 shl dir
                if ((visited[y][x] and bit) != 0) return true
                visited[y][x] = visited[y][x] or bit
            }
        }

        for (pos in visitedPositions) {
            val x = pos % width
            val y = pos / width

            if (x == startX && y == startY) continue
            if (lines[y][x] != '.') continue

            if (createsLoop(x, y)) {
                loopCount++
            }
        }

        return loopCount.toLong()
    }

}