package main

import java.io.File

class Day12 : Day {
    private data class Grid(val width: Int, val height: Int, val blockCounts: List<Int>)

    override fun processTextInputPartOne(filePath: String): Long {
        val text = File(filePath).readText()
        val (grids, shapes) = extractShapes(text)
        var result = 0L
        for (grid in grids) {
            val availableArea = grid.width * grid.height
            var usedArea = 0
            grid.blockCounts.forEachIndexed { index, i -> usedArea += shapes[index] * i }
            if (availableArea > usedArea) result++
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return -1L
    }

    private fun extractShapes(text: String): Pair<List<Grid>, List<Int>> {
        val chunks = text.trim().split("\n\n")
        val shapes = chunks.dropLast(1).map { rawShape ->
            rawShape.split("\n").drop(1).sumOf { singleLine ->
                singleLine.count {
                    it == '#'
                }
            }
        }
        val grids = chunks.last().trim().split("\n").map { gridString ->
            val (dimensions, counts) = gridString.split(":")
            val (width, height) = dimensions.split("x").map { it.trim().toInt() }
            Grid(width, height, counts.trim().split(Regex("\\s+")).map { it.toInt() })
        }
        return Pair(grids, shapes)
    }


}
