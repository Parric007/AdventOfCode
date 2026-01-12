package main

import java.io.File
import kotlin.math.max

class Day14: Day {
    private val simulationTime = 2503

    override fun processTextInputPartOne(filePath: String): String {
        var maxDistance = 0

        File(filePath).forEachLine { line ->
            val split = line.split(" ")
            val currReindeer = Reindeer(split[3].toInt(), split[6].toInt(), split[13].toInt())

            val cycleTime = currReindeer.restTime + currReindeer.sprintTime
            val remainder = simulationTime % cycleTime

            val timeFlown = (simulationTime / cycleTime) * currReindeer.sprintTime + minOf(remainder, currReindeer.sprintTime)
            maxDistance = max(maxDistance, timeFlown * currReindeer.speed)
        }

        return maxDistance.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val reindeerList = File(filePath).readLines().map { it.toReindeer() }

        repeat(simulationTime) {
            reindeerList.forEach { reindeer ->
                if (reindeer.hasEnergy) reindeer.distance += reindeer.speed

                reindeer.timeLeft--
                if (reindeer.timeLeft == 0) {
                    reindeer.hasEnergy = !reindeer.hasEnergy
                    reindeer.timeLeft = if (reindeer.hasEnergy) reindeer.sprintTime else reindeer.restTime
                }
            }

            val maxDistance = reindeerList.maxOf { it.distance }
            reindeerList.filter { it.distance == maxDistance }.forEach { it.currentPoints++ }
        }

        return reindeerList.maxOf { it.currentPoints }.toString()
    }

    private data class Reindeer(val speed: Int,
                                val sprintTime: Int,
                                val restTime: Int,
                                var timeLeft: Int = sprintTime,
                                var distance: Int = 0,
                                var hasEnergy: Boolean = true,
                                var currentPoints: Int = 0
    )

    private fun String.toReindeer(): Reindeer {
        val split = this.split(" ")
        return Reindeer(split[3].toInt(), split[6].toInt(), split[13].toInt())
    }
}