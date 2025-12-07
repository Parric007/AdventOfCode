package main

import java.io.File

class Day4 : Day {

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        val grid = File(filePath).readLines().map { it.toCharArray() }.toTypedArray()

        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (grid[x][y] != 'X') continue

                if (y + 3 < grid[x].size && checkAdjacentChars(grid, x, y, 0, 1)) result++

                if (x + 3 < grid.size && checkAdjacentChars(grid, x, y, 1, 0)) result++

                if (y > 2 && checkAdjacentChars(grid, x, y, 0, -1)) result++

                if (x > 2 && checkAdjacentChars(grid, x, y, -1, 0)) result++

                if (y + 3 < grid[x].size && x + 3 < grid.size && checkAdjacentChars(grid, x, y, 1, 1)) result++

                if (y + 3 < grid[x].size && x > 2 && checkAdjacentChars(grid, x, y, -1, 1)) result++

                if (y > 2 && x + 3 < grid.size && checkAdjacentChars(grid, x, y, 1, -1)) result++

                if (y > 2 && x > 2 && checkAdjacentChars(grid, x, y, -1, -1)) result++
            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        val grid = File(filePath).readLines().map { it.toCharArray() }.toTypedArray()

        for (x in 1 until grid.size - 1) {
            for (y in 1 until grid[x].size - 1) {
                if (grid[x][y] != 'A') continue
                if (checkForChars(grid, x, y, 'M') && checkForChars(grid, x, y, 'S')) result++
            }
        }
        return result
    }

    private fun checkAdjacentChars(grid: Array<CharArray>, x: Int, y: Int, directionX: Int, directionY: Int): Boolean {
        if (grid[x + directionX][y + directionY] != 'M') return false
        if (grid[x + 2 * directionX][y + 2 * directionY] != 'A') return false
        if (grid[x + 3 * directionX][y + 3 * directionY] != 'S') return false
        return true
    }

    private fun checkForChars(grid: Array<CharArray>, x: Int, y: Int, toCheck: Char): Boolean {
        val aList = mutableListOf<Pair<Int,Int>>()
        for (i in intArrayOf(-1, 1)){
            for (ii in intArrayOf(-1, 1)) {
                if (grid[x + i][y + ii] == toCheck) {
                    aList.add(Pair(i, ii))
                }
            }
        }
        return aList.size == 2 && aList.sumOf { pair -> pair.first * pair.second } == 0
    }
}