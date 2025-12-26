package main

import java.io.File
import kotlin.math.floor

class Day14: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val robots = getRobots(filePath)
        for (r in robots) {
            r.moveNSteps(100)
        }
        val topLeft = robots.filter { it.position.x < middleX && it.position.y < middleY }
        val topRight = robots.filter { it.position.x > middleX && it.position.y < middleY }
        val bottomLeft = robots.filter { it.position.x < middleX && it.position.y > middleY }
        val bottomRight = robots.filter { it.position.x > middleX && it.position.y > middleY }
        return (topLeft.size * topRight.size * bottomRight.size * bottomLeft.size).toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val robots = getRobots(filePath)

        var bestTime = 0
        var bestScore = 0

        // 10403 is LCM of 101 and 103, that means after 10403 steps it repeats
        for (t in 1..10403) {
            for (r in robots) {
                r.moveOneStep()
            }

            val score = longestVerticalRun(robots)
            if (score > bestScore) {
                bestScore = score
                bestTime = t
                if (score > 15) break // Early exit
            }
        }

        return bestTime.toLong()
    }

    private fun getRobots(filePath: String): List<Robot> {
        return File(filePath).readLines().map { robotString ->
            Robot(
                position =  Vector(
                    robotString.substringAfter("=").substringBefore(",").toInt(),
                    robotString.substringAfter(",").substringBefore(" ").toInt()),
                velocity = Vector(
                    robotString.substringAfterLast("=").substringBefore(",").toInt(),
                    robotString.substringAfterLast(",").toInt()
                )
            )
        }
    }

    private data class Robot(var position: Vector, val velocity: Vector) {
        fun moveOneStep() {
            position.x = (position.x + velocity.x).mod(X_DIMENSION)
            position.y = (position.y + velocity.y).mod(Y_DIMENSION)
        }

        fun moveNSteps(n: Int) {
            position.x = (position.x + velocity.x * n).mod(X_DIMENSION)
            position.y = (position.y + velocity.y * n).mod(Y_DIMENSION)
        }
    }

    private data class Vector(var x: Int, var y: Int)

    companion object {
        private const val X_DIMENSION = 101
        private const val Y_DIMENSION = 103

        private val middleX = floor(X_DIMENSION / 2.0)
        private val middleY = floor(Y_DIMENSION / 2.0)
    }

    private val columns = Array(X_DIMENSION) { BooleanArray(Y_DIMENSION) }

    private fun longestVerticalRun(robots: List<Robot>): Int {
        for (robot in robots) {
            columns[robot.position.x][robot.position.y] = true
        }

        var best = 0
        for (x in 0 until X_DIMENSION) {
            var current = 0
            for (y in 0 until Y_DIMENSION) {
                if (columns[x][y]) {
                    current++
                    if (current > best) best = current
                } else {
                    current = 0
                }
            }
            columns[x].fill(false)
        }

        return best
    }
}