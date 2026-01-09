package main

import java.io.File

class Day11: Day {

    private var firstNewPassword = ""

    override fun processTextInputPartOne(filePath: String): String {
        val startCode = File(filePath).readText().toCharArray()
        var newPassword = incString(startCode)

        while (!isValid(newPassword)) {
            newPassword = incString(newPassword)
        }

        firstNewPassword = String(newPassword)
        return firstNewPassword
    }

    override fun processTextInputPartTwo(filePath: String): String {
        var newPassword = incString(firstNewPassword.toCharArray())

        while (!isValid(newPassword)) {
            newPassword = incString(newPassword)
        }

        return String(newPassword)
    }

    private fun incString(str: CharArray): CharArray {
        var toInc = str.lastIndex

        while (toInc >= 0) {
            str[toInc]++

            if (str[toInc] > 'z') {
                str[toInc] = 'a'
                toInc--
                continue

            }

            if (str[toInc] in invalidChars) {
                str[toInc]++
                for (i in toInc + 1 until str.size) {
                    str[i] = 'a'
                }
            }
            return str
        }

        return charArrayOf('a') + str
    }

    private fun isValid(toCheck: CharArray): Boolean {
        var foundThreeStraight = false
        var foundFirstPair = '.'
        var foundSecondPair = '.'

        var i = 0

        while (i < toCheck.size - 1) {
            if (i < toCheck.size - 3 && toCheck[i] + 1 == toCheck[i + 1] && toCheck[i] + 2 == toCheck[i + 2]) {
                foundThreeStraight = true
            }

            if (toCheck[i] == toCheck[i + 1]) {
                if (foundFirstPair == '.') foundFirstPair = toCheck[i]
                else if (foundFirstPair != toCheck[i]) foundSecondPair = toCheck[i]
            }

            i++
        }

        return foundThreeStraight && foundFirstPair != '.' && foundSecondPair != '.'
    }

    private companion object {
        val invalidChars = setOf('l', 'o', 'i')
    }

}