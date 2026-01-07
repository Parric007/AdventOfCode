package main

import java.io.File

class Day5: Day {
    override fun processTextInputPartOne(filePath: String): String {
        return File(filePath).readLines().count { isNicePart1(it) }.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        return File(filePath).readLines().count { isNicePart2(it) }.toString()
    }

    private fun isNicePart1(str: String): Boolean {
        var vowelCount = 0
        var letterTwice = false

        str.forEachIndexed { index, c ->
            if (vowels.contains(c)) vowelCount++

            if (index < str.lastIndex) {
                val nextChar = str[index + 1]
                val nextTwoChars = "$c$nextChar"
                if (badStrings.contains(nextTwoChars)) {
                    return false
                }
                if (c == nextChar) letterTwice = true
            }
        }

        return vowelCount > 2 && letterTwice
    }

    private fun isNicePart2(str: String): Boolean {
        return hasNonOverlappingPair(str) && hasRepeatingWithGap(str)
    }

    private fun hasRepeatingWithGap(str: String): Boolean {
        for (i in 0 until str.length - 2) {
            if (str[i] == str[i + 2]) {
                return true
            }
        }
        return false
    }

    private fun hasNonOverlappingPair(str: String): Boolean {
        for (i in 0 until str.length - 3) {
            val a = str[i]
            val b = str[i + 1]

            for (j in i + 2 until str.length - 1) {
                if (str[j] == a && str[j + 1] == b) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        val badStrings = setOf("ab", "cd", "pq", "xy")
        val vowels = setOf('a', 'e', 'i', 'o', 'u')
    }
}