package main

import java.io.File
import java.util.PriorityQueue

class Day16 : Day {

    private var part1Solution: Long? = null
    private var part2Solution: Long? = null

    override fun processTextInputPartOne(filePath: String): Long {
        solve(filePath)
        return part1Solution!!
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return part2Solution!!
    }

    private fun solve(filePath: String) {
        val grid = File(filePath).readLines().map { it.toCharArray() }
        val h = grid.size
        val w = grid[0].size

        var sx = 0; var sy = 0
        var ex = 0; var ey = 0

        for (y in 0 until h)
            for (x in 0 until w)
                when (grid[y][x]) {
                    'S' -> { sx = x; sy = y }
                    'E' -> { ex = x; ey = y }
                }

        fun encode(x: Int, y: Int, d: Int) = (y * w + x) * 4 + d
        fun xOf(s: Int) = (s / 4) % w
        fun yOf(s: Int) = (s / 4) / w
        fun dOf(s: Int) = s and 3

        val dx = intArrayOf(0, 1, 0, -1)
        val dy = intArrayOf(-1, 0, 1, 0)

        val totalStates = w * h * 4
        val inf = Int.MAX_VALUE

        val dist = IntArray(totalStates) { inf }

        val parents = Array(totalStates) { IntArray(4) }
        val parentCount = IntArray(totalStates)

        val pq = PriorityQueue<Node>(compareBy { it.cost })

        val start = encode(sx, sy, 1) // EAST
        dist[start] = 0
        pq.add(Node(start, 0))

        while (pq.isNotEmpty()) {
            val (s, cost) = pq.poll()
            if (cost != dist[s]) continue

            val x = xOf(s)
            val y = yOf(s)
            val d = dOf(s)

            // forward
            val nx = x + dx[d]
            val ny = y + dy[d]
            if (nx in 0 until w && ny in 0 until h && grid[ny][nx] != '#') {
                relax(s, encode(nx, ny, d), cost + 1, dist, parents, parentCount, pq)
            }

            // left
            relax(s, encode(x, y, (d + 3) and 3), cost + 1000, dist, parents, parentCount, pq)

            // right
            relax(s, encode(x, y, (d + 1) and 3), cost + 1000, dist, parents, parentCount, pq)
        }

        var best = inf
        val endStates = IntArray(4)
        var endCount = 0

        for (d in 0..3) {
            val s = encode(ex, ey, d)
            val c = dist[s]
            if (c < best) {
                best = c
                endCount = 0
                endStates[endCount++] = s
            } else if (c == best) {
                endStates[endCount++] = s
            }
        }

        val seenState = BooleanArray(totalStates)
        val seenTile = BooleanArray(w * h)
        val stack = IntArray(totalStates)
        var sp = 0

        repeat(endCount) { stack[sp++] = endStates[it] }

        while (sp > 0) {
            val s = stack[--sp]
            if (seenState[s]) continue
            seenState[s] = true
            seenTile[s / 4] = true

            for (i in 0 until parentCount[s]) {
                stack[sp++] = parents[s][i]
            }
        }

        var tiles = 0
        for (b in seenTile) if (b) tiles++

        part1Solution = best.toLong()
        part2Solution = tiles.toLong()
    }

    private fun relax(
        from: Int,
        to: Int,
        newCost: Int,
        dist: IntArray,
        parents: Array<IntArray>,
        parentCount: IntArray,
        pq: PriorityQueue<Node>
    ) {
        val old = dist[to]
        when {
            newCost < old -> {
                dist[to] = newCost
                parentCount[to] = 1
                parents[to][0] = from
                pq.add(Node(to, newCost))
            }
            newCost == old -> {
                if (parentCount[to] == parents[to].size) {
                    parents[to] = parents[to].copyOf(parents[to].size * 2)
                }
                parents[to][parentCount[to]++] = from
            }
        }
    }

    private data class Node(val state: Int, val cost: Int)
}

