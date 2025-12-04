package main

fun main() {
    val currentDay = 1
    val dayClass = Day1()

    try {
        println("Part one: ${dayClass.processTextInputPartOne("./inputFiles/day${currentDay}Timo.txt")}")
        println("Part two: ${dayClass.processTextInputPartTwo("./inputFiles/day${currentDay}Timo.txt")}")
    } catch (e: Exception) {
        println(e.message)
        println("Day: $currentDay is not valid")
    }

}