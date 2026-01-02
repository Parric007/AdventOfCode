package main

import java.io.File
import java.io.OutputStreamWriter
import kotlin.time.Duration
import kotlin.time.measureTime

fun main() {
    val daysArray: List<Day> = listOf(Day1(), Day2(), Day3(), Day4(), Day5(), Day6(), Day7(), Day8(), Day9(), Day10(), Day11(), Day12(),
                                        Day13(), Day14(), Day15(), Day16(), Day17(), Day18(), Day19(), Day20(), Day21(), Day22(), Day23(),
                                        Day24(), Day25())

    val results = File("./README.md")
    val averageNum = 1000
    results.delete()
    results.createNewFile()
    val writer = results.writer()
    createHeader(writer)

    var sumTime: Duration = Duration.ZERO
    for ((index, day) in daysArray.withIndex())  {
        val dayNumber = index + 1
        try {
            var resultPart1: Long = 0
            var totalTimePart1 = Duration.ZERO

            repeat (averageNum) {
                totalTimePart1 += measureTime {
                    resultPart1 = day.processTextInputPartOne("./inputFiles/Day$dayNumber.txt")
                }
            }
            val timeForPart1 = totalTimePart1 / averageNum.toDouble()

            println("Day ${index + 1}, Part one: $resultPart1 in $timeForPart1 over $averageNum retries")
            writer.write("## Day $dayNumber\n" +
                    "### Part one\n" +
                    "\n" +
                    "Result: $resultPart1 \\\n" +
                    "Time: $timeForPart1\n")
            sumTime += timeForPart1

            var resultPart2: Long = 0
            var totalTimePart2 = Duration.ZERO
            repeat (averageNum) {
                totalTimePart2 += measureTime {
                    resultPart2 = day.processTextInputPartTwo("./inputFiles/Day$dayNumber.txt")
                }
            }
            val timeForPart2 = totalTimePart2 / averageNum.toDouble()
            println("Day ${index + 1}, Part two: $resultPart2 in $timeForPart2  over $averageNum retries")
            writer.write("### Part two\n" +
                    "\n" +
                    "Result: $resultPart2 \\\n" +
                    "Time: $timeForPart2\n\n")
            sumTime += timeForPart2
        } catch (ex: Throwable) {
            println("Error on day $dayNumber: $ex")
        }
    }
    println("All runs completed. Total time: $sumTime")

    writer.write("Total time:\n" +
            "$sumTime \\\n"+
            "Each run was averaged over $averageNum runs\n\n")

    writer.write("For Josy. Forever")

    writer.close()
}

fun createHeader(fileWriter: OutputStreamWriter) {
    fileWriter.write("# Advent of Code 2024 Results\n" +
            "By Liam\n")
}