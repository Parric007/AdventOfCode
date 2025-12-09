package main

import java.io.File
import kotlin.math.abs

class Day9: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var biggestSquare = 0L

        val lines  = File(filePath).readLines().map {
            val (x, y) = it.split(",")
            return@map (x.toLong() to y.toLong())
        }

        for (i in lines.indices) {
            for (j in i + 1 until lines.size) {
                val squareSize = (abs(lines[i].first - lines[j].first) + 1) * (abs(lines[i].second - lines[j].second) + 1)
                if (squareSize > biggestSquare) {
                    biggestSquare = squareSize
                }
            }
        }

        return biggestSquare
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val polygon  = File(filePath).readLines().map {
            val (x, y) = it.split(",")
            return@map Point(x.toInt(), y.toInt())
        }

        val xs = polygon.map { it.x }.distinct().sorted().toIntArray()
        val ys = polygon.map { it.y }.distinct().sorted().toIntArray()

        return largestRectangleInOrthogonalPolygon(xs, ys, xs.size)
    }

    class Point(val x: Int, val y: Int)

    private fun largestRectangleInOrthogonalPolygon(px: IntArray, py: IntArray, n: Int): Long {
        val xsTmp = px.copyOf()
        val ysTmp = py.copyOf()
        java.util.Arrays.sort(xsTmp)
        java.util.Arrays.sort(ysTmp)

        var xCount = 0
        var yCount = 0
        for (i in xsTmp.indices) {
            if (i == 0 || xsTmp[i] != xsTmp[i - 1]) xsTmp[xCount++] = xsTmp[i]
            if (i == 0 || ysTmp[i] != ysTmp[i - 1]) ysTmp[yCount++] = ysTmp[i]
        }

        if (xCount < 2 || yCount < 2) return 0L

        val xs = xsTmp.copyOf(xCount)
        val ys = ysTmp.copyOf(yCount)
        val nx = xs.size
        val ny = ys.size

        val grid = Array(nx - 1) { ByteArray(ny - 1) }
        for (r in 0 until ny - 1) {
            val yMid = ys[r] + (ys[r + 1] - ys[r]) / 2.0
            for (c in 0 until nx - 1) {
                val xMid = xs[c] + (xs[c + 1] - xs[c]) / 2.0
                if (pointInPolygonDouble(xMid, yMid, px, py, n)) grid[c][r] = 1
            }
        }
        val heights = IntArray(nx - 1)
        var best = 0L

        for (bottom in 0 until ny - 1) {
            for (i in 0 until nx - 1) heights[i] = 0
            for (top in bottom until ny - 1) {
                val slabH = ys[top + 1] - ys[top]
                for (c in 0 until nx - 1) {
                    if (grid[c][top].toInt() == 1) heights[c] += slabH
                    else heights[c] = 0
                }
                val stack = IntArray(nx)
                var sp = 0
                var x = 0
                while (x <= nx - 1) {
                    val h = if (x < nx - 1) heights[x].toLong() else 0L
                    if (sp == 0 || h >= heights[stack[sp - 1]].toLong()) {
                        stack[sp++] = x
                        x++
                    } else {
                        val tp = stack[--sp]
                        val ht = heights[tp].toLong()
                        val left = if (sp == 0) 0 else stack[sp - 1] + 1
                        val right = x - 1
                        val w = (xs[right + 1] - xs[left]).toLong()
                        val area = ht * w
                        if (area > best) best = area
                    }
                }
            }
        }

        return best
    }

    private fun pointInPolygonDouble(x: Double, y: Double, px: IntArray, py: IntArray, n: Int): Boolean {
        var inside = false
        var j = n - 1
        for (i in 0 until n) {
            val xi = px[i].toDouble()
            val yi = py[i].toDouble()
            val xj = px[j].toDouble()
            val yj = py[j].toDouble()
            if ((yi > y) != (yj > y) && x < (xj - xi) * (y - yi) / (yj - yi) + xi) inside = !inside
            j = i
        }
        return inside
    }

}

