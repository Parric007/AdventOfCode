package main

import java.io.File

class Day8: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val positionOfAntiNodes = mutableSetOf<Point>()
        val grid = File(filePath).readLines()
        val xDim = grid[0].length
        val yDim = grid.size

        buildFrequencyMap(grid).forEach { (_, listOfCoordinates) ->
            for (i in listOfCoordinates.indices) {
                for (j in i+1  .. listOfCoordinates.lastIndex) {
                    val pos1 = listOfCoordinates[i]
                    val pos2 = listOfCoordinates[j]

                    val p1 = Point(2 * pos1.x - pos2.x, 2 * pos1.y - pos2.y)
                    val p2 = Point(2 * pos2.x - pos1.x, 2 * pos2.y - pos1.y)
                    if (p1.x in 0 until xDim && p1.y in 0 until yDim) {
                        positionOfAntiNodes.add(p1)
                    }
                    if (p2.x in 0 until xDim && p2.y in 0 until yDim) {
                        positionOfAntiNodes.add(p2)
                    }
                }
            }
        }

        return positionOfAntiNodes.size.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val positionOfAntiNodes = mutableSetOf<Point>()
        val grid = File(filePath).readLines()
        val xDim = grid[0].length
        val yDim = grid.size

        buildFrequencyMap(grid).forEach { (_, listOfCoordinates) ->
            for (i in listOfCoordinates.indices) {
                for (j in i+1  .. listOfCoordinates.lastIndex) {
                    addAntiNodesPart2(listOfCoordinates[i], listOfCoordinates[j], xDim, yDim, positionOfAntiNodes)
                }
            }
        }

        return positionOfAntiNodes.size.toLong()
    }

    data class Point(val x: Int, val y: Int)

    private fun buildFrequencyMap(grid: List<String>): Map<Char, MutableList<Point>> {
        val frequenciesMap = mutableMapOf<Char, MutableList<Point>>()
        grid.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c != '.') frequenciesMap
                    .getOrPut(c) { mutableListOf()}
                    .add(Point(x, y))
            }
        }
        return frequenciesMap.filterValues { it.size >= 2 }
    }

    private fun gcd(a: Int, b: Int): Int =
        if (b == 0) kotlin.math.abs(a) else gcd(b, a % b)

    private fun addAntiNodesPart2(
        a: Point,
        b: Point,
        xDim: Int,
        yDim: Int,
        positionOfAntiNodes: MutableSet<Point>
    ) {
        val dx = b.x - a.x
        val dy = b.y - a.y

        val g = gcd(dx, dy)
        val stepX = dx / g
        val stepY = dy / g

        var x = a.x
        var y = a.y
        while (x in 0 until xDim && y in 0 until yDim) {
            positionOfAntiNodes.add(Point(x, y))
            x += stepX
            y += stepY
        }

        x = a.x - stepX
        y = a.y - stepY
        while (x in 0 until xDim && y in 0 until yDim) {
            positionOfAntiNodes.add(Point(x, y))
            x -= stepX
            y -= stepY
        }
    }
}