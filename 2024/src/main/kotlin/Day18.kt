package main

import java.io.File

class Day18: Day {
    private val directions = arrayOf(
        1 to 0,
        -1 to 0,
        0 to 1,
        0 to -1
    )

    override fun processTextInputPartOne(filePath: String): Long {
        val dimensions = 71

        val memoryGrid = Array(dimensions) { BooleanArray(dimensions) }

        val lines = File(filePath).readLines()
        for (i in 0 until 1024) {
            val (x, y) = lines[i].split(",").map { it.toInt() }
            memoryGrid[y][x] = true
        }

        return bfs(memoryGrid)!!.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val lines = File(filePath).readLines()

        val blocks = lines.map {
            val (x, y) = it.split(",").map(String::toInt)
            x to y
        }

        var lo = 0
        var hi = blocks.size

        while (lo < hi) {
            val mid = (lo + hi) / 2

            if (reachable(blocks, mid)) {
                lo = mid + 1
            } else {
                hi = mid
            }
        }

        // val (x, y) = blocks[lo - 1]
        // println("$x,$y")
        return 0L
    }

    private data class Point(val x: Int, val y: Int)

    private fun bfs(blocked: Array<BooleanArray>): Int? {
        val visited = Array(71) { BooleanArray(71) }
        val queue = ArrayDeque<Point>()

        if (blocked[0][0]) return null

        queue.add(Point(0, 0))
        visited[0][0] = true

        var steps = 0

        while (queue.isNotEmpty()) {
            repeat(queue.size) {
                val (x, y) = queue.removeFirst()

                if (x == 70 && y == 70) {
                    return steps
                }

                for ((dx, dy) in directions) {
                    val nx = x + dx
                    val ny = y + dy

                    if (nx !in 0..70 || ny !in 0..70) continue
                    if (blocked[ny][nx]) continue
                    if (visited[ny][nx]) continue

                    visited[ny][nx] = true
                    queue.add(Point(nx, ny))
                }
            }
            steps++
        }

        return null
    }

    private fun reachable(blocks: List<Pair<Int, Int>>, count: Int): Boolean {
        val blocked = Array(71) { BooleanArray(71) }

        for (i in 0 until count) {
            val (x, y) = blocks[i]
            blocked[y][x] = true
        }

        return bfs(blocked) != null
    }
}