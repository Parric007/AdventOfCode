package main

import java.io.File
import java.util.*

class Day8: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val coordinates = File(filePath).readText().split("\n").map { line ->
            val (x, y, z) = line.split(",")
            Point(x.toInt(), y.toInt(), z.toInt())
        }

        val heapForDist = PriorityQueue<Triple<Long, Int, Int>>(compareByDescending {it.first})

        for (i in coordinates.indices) {
            for (j in i + 1 until coordinates.size) {
                val distance = coordinates[i].squaredDistanceTo(coordinates[j])
                heapForDist.offer(Triple(distance, i, j))
                if (heapForDist.size > 1000 ) {
                    heapForDist.poll()
                }
            }
        }

        val numOfCoordinates = coordinates.size
        val uf = UnionFind(numOfCoordinates)
        while (heapForDist.isNotEmpty()) {
            val (_, p1, p2) = heapForDist.poll()
            uf.union(p1, p2)
        }

        val freq = IntArray(numOfCoordinates)
        for (i in 0 until numOfCoordinates) {
            freq[uf.find(i)]++
        }

        return freq.sortedDescending().take(3).fold(1L){ acc, value -> acc * value}
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val coordinates = File(filePath).readText().split("\n").map { line ->
            val (x, y, z) = line.split(",")
            Point(x.toInt(), y.toInt(), z.toInt())
        }

        val numOfCoordinates = coordinates.size
        val pq = PriorityQueue<Triple<Long, Int, Int>>(compareBy { it.first })

        for (i in 0 until numOfCoordinates) {
            for (j in i + 1 until numOfCoordinates) {
                pq.add(Triple(coordinates[i].squaredDistanceTo(coordinates[j]), i, j))
            }
        }

        val unionFind = UnionFind(numOfCoordinates)
        var components = numOfCoordinates
        var lastMerge: Pair<Int, Int>? = null

        while (components > 1) {
            val (_, a, b) = pq.poll()
            if (unionFind.union(a, b)) {
                components--
                lastMerge = a to b
            }
        }

        val (i, j) = lastMerge!!
        val x1 = coordinates[i].x
        val x2 = coordinates[j].x
        return minOf(x1, x2).toLong() * maxOf(x1, x2).toLong()
    }

    data class Point(val x: Int, val y: Int, val z: Int) {
        // Because why should we calculate the square root if we don't have to ;)
        fun squaredDistanceTo(other: Point): Long {
            val dx = (x - other.x).toLong()
            val dy = (y - other.y).toLong()
            val dz = (z - other.z).toLong()
            return dx * dx + dy * dy + dz * dz
        }
    }

    class UnionFind(n: Int) {
        private val parent = IntArray(n) { it }
        private val size = IntArray(n) { 1 }

        fun find(a: Int): Int {
            var x = a
            while (parent[x] != x) {
                parent[x] = parent[parent[x]]
                x = parent[x]
            }
            return x
        }

        fun union(a: Int, b: Int): Boolean {
            var ra = find(a)
            var rb = find(b)
            if (ra == rb) return false
            if (size[ra] < size[rb]) { val tmp = ra; ra = rb; rb = tmp }
            parent[rb] = ra
            size[ra] += size[rb]
            return true
        }
    }
}

