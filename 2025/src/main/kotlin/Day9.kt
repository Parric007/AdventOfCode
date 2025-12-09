package main

import java.io.File
import kotlin.math.abs

class Day9 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var biggestSquare = 0L

        val lines = File(filePath).readLines().map {
            val (x, y) = it.split(",")
            return@map Point(x.toLong(), y.toLong())
        }

        for (i in lines.indices) {
            for (j in i + 1 until lines.size) {
                val squareSize = (abs(lines[i].x - lines[j].x) + 1) * (abs(lines[i].y - lines[j].y) + 1)
                if (squareSize > biggestSquare) {
                    biggestSquare = squareSize
                }
            }
        }

        return biggestSquare
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var biggestSquare = 0L
        val points = File(filePath).readLines().map {
            val (x, y) = it.split(",")
            Point(x.toLong(), y.toLong())
        }

        val polygonSpatialIndex = buildPolygonIndex(points)
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val point1 = points[i]
                val point2 = points[j]
                if (rectangleInsidePolygonIndexed(point1.x, point1.y, point2.x, point2.y, polygonSpatialIndex)) {
                    val squareSize = (abs(point1.x - point2.x) + 1) * (abs(point1.y - point2.y) + 1)
                    if (squareSize > biggestSquare) {
                        biggestSquare = squareSize
                    }
                }
            }
        }

        return biggestSquare
    }

    data class Point(val x: Long, val y: Long)
    data class Edge(val start: Point, val end: Point)

    class PolygonSpatialIndex(
        edgesIn: List<Edge>,
        val minX: Long,
        val maxX: Long,
        val minY: Long,
        val maxY: Long,
        private val gridSize: Int = 200
    ) {
        private val cellW = (maxX - minX).toDouble() / gridSize
        private val cellH = (maxY - minY).toDouble() / gridSize
        private val grid = Array(gridSize) { Array(gridSize) { mutableListOf<Edge>() } }

        private val edges: List<Edge> = edgesIn.filter { edge ->
            !(edge.start.x == edge.end.x && edge.start.y == edge.end.y)
        }

        init {
            for (e in edges) {
                val ex1 = minOf(e.start.x, e.end.x)
                val ex2 = maxOf(e.start.x, e.end.x)
                val ey1 = minOf(e.start.y, e.end.y)
                val ey2 = maxOf(e.start.y, e.end.y)

                val gx1 = clamp(gridX(ex1), gridSize - 1)
                val gx2 = clamp(gridX(ex2), gridSize - 1)
                val gy1 = clamp(gridY(ey1), gridSize - 1)
                val gy2 = clamp(gridY(ey2), gridSize - 1)

                for (gx in gx1..gx2) for (gy in gy1..gy2) grid[gx][gy].add(e)
            }
        }

        private fun gridX(x: Long): Int = ((x - minX) / cellW).toInt()
        private fun gridY(y: Long): Int = ((y - minY) / cellH).toInt()
        private fun clamp(v: Int, hi: Int) = v.coerceIn(0, hi)

        fun queryUnique(x1: Long, y1: Long, x2: Long, y2: Long): List<Edge> {
            val rx1 = minOf(x1, x2)
            val rx2 = maxOf(x1, x2)
            val ry1 = minOf(y1, y2)
            val ry2 = maxOf(y1, y2)

            val gx1 = clamp(gridX(rx1), gridSize - 1)
            val gx2 = clamp(gridX(rx2), gridSize - 1)
            val gy1 = clamp(gridY(ry1), gridSize - 1)
            val gy2 = clamp(gridY(ry2), gridSize - 1)

            val set = LinkedHashSet<Edge>()
            for (gx in gx1..gx2) for (gy in gy1..gy2) for (e in grid[gx][gy]) set.add(e)

            return set.filter { e ->
                !(maxOf(e.start.x, e.end.x) < rx1 || minOf(e.start.x, e.end.x) > rx2 || maxOf(e.start.y, e.end.y) < ry1 || minOf(
                    e.start.y, e.end.y
                ) > ry2)
            }
        }
    }

    private fun orientation(a: Point, b: Point, c: Point): Int {
        val v = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
        return when {
            v > 0L -> 1
            v < 0L -> -1
            else -> 0
        }
    }

    private fun pointOnSegment(a: Point, b: Point, p: Point): Boolean {
        val cross = (b.x - a.x) * (p.y - a.y) - (p.x - a.x) * (b.y - a.y)
        if (cross != 0L) return false
        return p.x in minOf(a.x, b.x)..maxOf(a.x, b.x) && p.y in minOf(a.y, b.y)..maxOf(a.y, b.y)
    }

    private fun segmentsCrossProperly(a: Point, b: Point, c: Point, d: Point): Boolean {
        val o1 = orientation(a, b, c)
        val o2 = orientation(a, b, d)
        val o3 = orientation(c, d, a)
        val o4 = orientation(c, d, b)
        return (o1 * o2 < 0L && o3 * o4 < 0L)
    }

    private fun pointInsideOrBoundaryIndexed(
        p: Point, psi: PolygonSpatialIndex
    ): Boolean {

        if (p.x < psi.minX || p.x > psi.maxX || p.y < psi.minY || p.y > psi.maxY) {
            return false
        }

        val candidates = psi.queryUnique(p.x, p.y, psi.maxX, p.y)

        for ((a, b) in candidates) {
            if (pointOnSegment(a, b, p)) return true
        }

        var count = 0
        for ((pointA, pointB) in candidates) {
            if (pointA.y == pointB.y) continue

            val yMin = minOf(pointA.y, pointB.y)
            val yMax = maxOf(pointA.y, pointB.y)

            if (p.y !in yMin..<yMax) continue

            val xInt =
                pointA.x.toDouble() + (p.y - pointA.y).toDouble() * (pointB.x - pointA.x).toDouble() / (pointB.y - pointA.y).toDouble()
            if (xInt > p.x) count++
        }

        return (count % 2 == 1)
    }

    private fun rectangleInsidePolygonIndexed(
        x1: Long,
        y1: Long,
        x2: Long,
        y2: Long,
        psi: PolygonSpatialIndex
    ): Boolean {
        for (c in listOf(
            Point(x1, y1), Point(x1, y2), Point(x2, y1), Point(x2, y2)
        )) {
            if (!pointInsideOrBoundaryIndexed(c, psi)) return false
        }

        val rectangleEdges = listOf(
            Edge(Point(x1, y1), Point(x2, y1)),
            Edge(Point(x2, y1), Point(x2, y2)),
            Edge(Point(x2, y2), Point(x1, y2)),
            Edge(Point(x1, y2), Point(x1, y1))
        )
        val polyEdges = psi.queryUnique(
            minOf(x1, x2), minOf(y1, y2), maxOf(x1, x2), maxOf(y1, y2)
        )

        for (rectEdge in rectangleEdges) {
            for (polyEdge in polyEdges) {
                if (segmentsCrossProperly(rectEdge.start, rectEdge.end, polyEdge.start, polyEdge.end)) {
                    return false
                }
            }
        }

        return true
    }

    private fun buildPolygonIndex(points: List<Point>): PolygonSpatialIndex {
        val edges = points.zip(points.drop(1)) { a, b -> Edge(a, b) } + Edge(points.last(), points.first())

        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        return PolygonSpatialIndex(edges, minX, maxX, minY, maxY)
    }
}

