package main

import java.io.File

class Day4 : Day {
    private val indicesDiffs = listOf(
        -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
    )

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        val grid = File(filePath).readLines().map { it.toCharArray() }.toTypedArray()

        for (i in grid.indices) {
            for (ii in grid[i].indices) {
                if (grid[i][ii] == '.') continue
                if (countNeighbors(grid, i, ii, indicesDiffs) < 4) result++
            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val grid = File(filePath).readLines().map { it.toCharArray() }.toTypedArray()

        val theQUEUE = ArrayDeque<Pair<Int, Int>>()

        for (i in grid.indices) for (j in 0 until grid[0].size) if (grid[i][j] == '@') theQUEUE.add(i to j)

        while (theQUEUE.isNotEmpty()) {
            val (x, y) = theQUEUE.removeFirst()
            if (grid[x][y] != '@') continue

            if (countNeighbors(grid, x, y, indicesDiffs) < 4) {
                grid[x][y] = '.'
                result++

                for ((dx, dy) in indicesDiffs) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in grid.indices && ny in 0 until grid[0].size) theQUEUE.add(nx to ny)
                }
            }
        }

        return result
    }

    private fun countNeighbors(grid: Array<CharArray>, x: Int, y: Int, indicesDiffs: List<Pair<Int, Int>>): Int {
        var counter = 0
        for ((dx, dy) in indicesDiffs) {
            val nx = x + dx
            val ny = y + dy
            if (nx in grid.indices && ny in 0 until grid[0].size && grid[nx][ny] == '@') counter++
        }
        return counter
    }
}