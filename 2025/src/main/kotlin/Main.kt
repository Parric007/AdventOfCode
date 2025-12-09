package main

import java.io.File
import java.io.OutputStreamWriter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

fun main() {
    val daysArray: List<Day> = listOf(Day1(), Day2(), Day3(), Day4(), Day5(), Day6(), Day7(), Day8(), Day9())

    val results = File("./README.md")
    results.delete()
    results.createNewFile()
    val writer = results.writer()
    createHeader(writer)

    var sumTime: Duration = 0.seconds

    for (i in 1..daysArray.size)  {
        try {
            var partOne: Long
            val timeForPart1 = measureTime {
                partOne = daysArray[i - 1].processTextInputPartOne("./inputFiles/Day${i}.txt")
            }
            println("Day $i, Part one: $partOne in $timeForPart1")
            writer.write("## Day $i\n" +
                    "### Part one\n" +
                    "\n" +
                    "Result: $partOne \\\n" +
                    "Time: $timeForPart1\n")
            sumTime += timeForPart1
            var partTwo: Long
            val timeForPart2 = measureTime {
                partTwo = daysArray[i - 1].processTextInputPartTwo("./inputFiles/Day${i}.txt")
            }
            println("Day $i, Part two: $partTwo in $timeForPart2")
            writer.write("### Part two\n" +
                    "\n" +
                    "Result: $partTwo \\\n" +
                    "Time: $timeForPart2\n\n")
            sumTime += timeForPart2
        } catch (ex: Throwable) {
            println("Error on day $i: $ex")
        }
    }

    writer.write("Total time:\n" +
            "$sumTime")

    writer.close()
}

fun createHeader(fileWriter: OutputStreamWriter) {
    fileWriter.write("# Advent of Code 2025 Results\n" +
            "By Liam\n")
}