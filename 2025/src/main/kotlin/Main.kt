package main

import kotlin.time.measureTime

fun main() {
    val daysArray: List<Day> = listOf(Day1(), Day2(), Day3())

    for (i in 1..daysArray.size)  {
        var dayOne: Long
        val timeForDay1 = measureTime {
            dayOne = daysArray[i - 1].processTextInputPartOne("./inputFiles/Day${i}.txt")
        }
        println("Day $i, Part one: $dayOne in $timeForDay1")
        var dayTwo: Long
        val timeForDay2 = measureTime {
            dayTwo = daysArray[i - 1].processTextInputPartTwo("./inputFiles/Day${i}.txt")
        }
        println("Day $i, Part two: $dayTwo in $timeForDay2")
    }
}