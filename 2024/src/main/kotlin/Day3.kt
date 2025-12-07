package main

import java.io.File

class Day3: Day {

    private val multiplicationRegex = Regex("""mul\((\d+),(\d+)\)""")

    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            result += calculateMultiplications(line)
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val regex = Regex("""(?:^|do\(\))(.*?)(?:don't\(\)|$)""", RegexOption.DOT_MATCHES_ALL)
        val text = File(filePath).readText()
        return regex.findAll(text).sumOf { matchResult ->calculateMultiplications(matchResult.groupValues[1]) }
    }

    private fun calculateMultiplications(line: String): Long {
        return multiplicationRegex.findAll(line).map { matchResult ->
            matchResult.groups[1]?.value?.toLong()?.times(matchResult.groups[2]?.value?.toLong()!!) ?: 0
        }.sum()
    }
}