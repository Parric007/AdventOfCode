package main

import java.io.File

class Day10: Day {

    override fun processTextInputPartOne(filePath: String): String {
        var sb = StringBuilder(File(filePath).readText())

        repeat(40) {
            sb = nextIteration(sb)
        }
        return sb.length.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        var sb = StringBuilder(File(filePath).readText())

        repeat(50) {
            sb = nextIteration(sb)
        }
        return sb.length.toString()
    }

    private fun nextIteration(sb: StringBuilder): StringBuilder {
        val toReturn = StringBuilder()

        var i = 0
        while (i < sb.length) {
            val current = sb[i]
            var count = 1
            while (i + count < sb.length && sb[i + count] == current) {
                count++
            }

            toReturn.append(count)
            toReturn.append(current)

            i += count
        }

        return toReturn
    }
}