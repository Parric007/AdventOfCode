package main

import java.io.File
import kotlin.math.absoluteValue

class Day20: Day {
    private val speedPositionMap = mutableMapOf<Point, Int>()

    private val directions = arrayOf(
        Point(1, 0),
        Point(-1, 0),
        Point(0, 1),
        Point(0, -1)
    )

    private val cheatOffsets: List<Point> = buildList {
        for (dx in -20..20) {
            for (dy in -20..20) {
                val dist = kotlin.math.abs(dx) + kotlin.math.abs(dy)
                if (dist in 1..20) {
                    add(Point(dx, dy))
                }
            }
        }
    }

    override fun processTextInputPartOne(filePath: String): Long {
        val grid = File(filePath).readLines().map { it.toCharArray() }

        speedPositionMap.clear()

        var startPos = Point(-1, -1)

        for (y in grid.indices) {
            if (y > grid[0].lastIndex) continue

            val char = grid[y][y]
            if (char == '#') continue

            startPos = Point(y, y)
            break
        }

        speedPositionMap[startPos] = 0

        exploreGrid(grid, startPos, 1)
        exploreGrid(grid, startPos, -1)

        var numOfCheats = 0L

        for ((key, currentValue) in speedPositionMap) {
            for (dir in directions) {
                val target = key + dir * 2
                val toCompare = speedPositionMap[target] ?: continue

                val diff = (toCompare - currentValue) - 2
                if (diff >= 100) {
                    numOfCheats++
                }
            }
        }

        return numOfCheats
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var numOfCheats = 0L

        for ((startPos, currentValue) in speedPositionMap) {
            for (offset in cheatOffsets) {
                val target = startPos + offset
                val toCompare = speedPositionMap[target] ?: continue

                if (toCompare <= currentValue) continue

                val distance = offset.x.absoluteValue + offset.y.absoluteValue
                val saved = (toCompare - currentValue) - distance

                if (saved >= 100) {
                    numOfCheats++
                }
            }
        }

        return numOfCheats
    }

    private data class Point(val x: Int, val y: Int) {

        operator fun plus(other: Point): Point {
            return Point(x + other.x, y + other.y)
        }

        operator fun times(factor: Int): Point {
            return Point(x * factor, y * factor)
        }
    }

    private fun get(grid: List<CharArray>, at: Point): Char {
        return grid[at.y][at.x]
    }

    private fun exploreGrid(grid: List<CharArray>, explorePosition: Point, nextExploreValue: Int) {

        var toExplore = explorePosition
        var exploreValue = nextExploreValue

        while (true) {
            for (dir in directions) {
                val nextPoint = toExplore + dir

                if (speedPositionMap.containsKey(nextPoint)) continue

                val nextChar = get(grid, nextPoint)

                if (nextChar == '#') {
                    continue
                }

                speedPositionMap[nextPoint] = exploreValue

                if (nextChar == 'S' || nextChar == 'E') {
                    return
                }
                toExplore = nextPoint
                exploreValue += nextExploreValue
            }
        }
    }
}