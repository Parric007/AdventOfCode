package main

import java.io.File

class Day15: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val (unparsedGrid, instructions) = File(filePath).readText().split("\n\n")

        var pointOfRobot = Point(-1, -1)

        val grid = unparsedGrid.split("\n").mapIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '@') pointOfRobot = Point(x, y)
            }
            line.toCharArray()
        }

        for (instruction in instructions) {
            val dir = getDirPoint(instruction) ?: continue
            val target = pointOfRobot + dir

            val nextFree = nextFreeCoordsPart1(grid, pointOfRobot, dir) ?: continue

            pointOfRobot = target

            if (nextFree != target) {
                set(grid, nextFree, 'O')
                set(grid, target, '.')
            }
        }

        return grid.foldIndexed(0L) { indexY, acc, boolArray ->
            acc + boolArray.foldIndexed(0L) { indexX, accX, bool ->
                if (bool == 'O') {
                    accX + (indexY) * 100 + indexX
                } else accX
            }
        }
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val (unparsedGrid, instructions) = File(filePath).readText().split("\n\n")

        val grid = unparsedGrid.split("\n").map { line ->
            line.flatMap { char ->
                when (char) {
                    '@' -> listOf('@', '.')
                    'O' -> listOf('[', ']')
                    '#' -> listOf('#', '#')
                    else -> listOf('.', '.')
                }
            }.toCharArray()
        }

        var pointOfRobot = Point(-1, -1)

        grid.forEachIndexed { y, chars -> chars.forEachIndexed { x, c -> if (c == '@') pointOfRobot = Point(x, y) }}

        for (instruction in instructions) {
            val dir = getDirPoint(instruction) ?: continue
            val target = pointOfRobot + dir

            val affectedBlocks = nextFreeCoordsPart2(grid, pointOfRobot, instruction, mutableSetOf()) ?: continue

            if (affectedBlocks.size == 1) {
                set(grid, pointOfRobot, '.')
                pointOfRobot = target
                set(grid, pointOfRobot, '@')
                continue
            }

            when (instruction) {
                '<' -> {
                    val chars = affectedBlocks.map { get(grid, it)!! }
                    for (x in 1..<affectedBlocks.size) {
                        set(grid, affectedBlocks.elementAt(x), chars[x - 1])
                    }
                    set(grid, pointOfRobot, '.')
                }
                '>' -> {
                    for (i in affectedBlocks.size - 1 downTo 1) {
                        set(grid, affectedBlocks.elementAt(i), get(grid, affectedBlocks.elementAt(i - 1))!!)
                    }
                    set(grid, pointOfRobot, '.')
                }
                '^' -> {
                    val sorted = affectedBlocks.sortedBy { it.y }
                    val chars = sorted.map { get(grid, it)!! }
                    for (i in sorted.indices) {
                        if (chars[i] == '.') continue
                        val blockToSet = Point(sorted[i].x, sorted[i].y - 1)
                        set(grid, blockToSet, chars[i])
                        set(grid, sorted[i], '.')
                    }
                }
                'v' -> {
                    val sorted = affectedBlocks.sortedByDescending { it.y }
                    val chars = sorted.map { get(grid, it)!! }

                    for (i in sorted.indices) {
                        if (chars[i] == '.') continue
                        val blockToSet = Point(sorted[i].x, sorted[i].y + 1)
                        set(grid, blockToSet, chars[i])
                        set(grid, sorted[i], '.')
                    }
                }
            }
            set(grid, pointOfRobot, '.')
            pointOfRobot = target
            set(grid, pointOfRobot, '@')
        }

        return grid.foldIndexed(0L) { indexY, acc, boolArray ->
            acc + boolArray.foldIndexed(0L) { indexX, accX, bool ->
                if (bool == '[') {
                    accX + (indexY) * 100 + indexX
                } else accX
            }
        }
    }

    private data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point): Point {
            return Point(x + other.x, y + other.y)
        }
    }

    private fun getDirPoint(char: Char): Point? {
        return when (char) {
            '^' -> Point(0, -1)
            'v' -> Point(0, 1)
            '<' -> Point(-1, 0)
            '>' -> Point(1, 0)
            else -> null
        }
    }

    private fun nextFreeCoordsPart1(grid: List<CharArray>, point: Point, dir: Point): Point? {
        val currentPoint = point + dir
        val pointToCheck = get(grid, currentPoint) ?: return null

        if (pointToCheck == '#') return null

        return if(pointToCheck == 'O') nextFreeCoordsPart1(grid, currentPoint, dir) else currentPoint
    }

    private fun nextFreeCoordsPart2(grid: List<CharArray>, point: Point, dir: Char, checked: MutableSet<Point>): MutableSet<Point>? {
        val currentPoint = point + getDirPoint(dir)!!

        if (currentPoint in checked) return checked
        checked.add(currentPoint)

        val pointToCheck = get(grid, currentPoint) ?: return null

        if (pointToCheck == '#') return null
        if (pointToCheck == '.') return checked

        return when (dir) {
            '^', 'v' -> {
                val next = nextFreeCoordsPart2(grid, currentPoint, dir, checked)

                when (pointToCheck) {
                    '[' -> {
                        val right = nextFreeCoordsPart2(grid, point + Point(1, 0), dir, checked
                        )
                        if (next != null && right != null) checked
                        else null
                    }
                    ']' -> {
                        val left = nextFreeCoordsPart2(grid, point + Point(-1, 0), dir, checked)
                        if (next != null && left != null) checked
                        else null
                    }
                    else -> null
                }
            }
            '<', '>' -> {
                val next = nextFreeCoordsPart2(grid, currentPoint, dir, checked)
                if (next != null) checked
                else null
            }
            else -> null
        }
    }

    private fun get(grid: List<CharArray>, point: Point): Char? {
        if (point.x !in 0..grid[0].lastIndex) return null
        if (point.y !in 0..grid.lastIndex) return null
        return grid[point.y][point.x]
    }

    private fun set(grid: List<CharArray>, point: Point, toSet: Char) {
        grid[point.y][point.x] = toSet
    }
}