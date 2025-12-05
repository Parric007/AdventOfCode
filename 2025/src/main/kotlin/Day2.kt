package main

import java.io.File

class Day2 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            // Should only be one
            for (idPair in line.split(',').listIterator()) {
                val splitPair = idPair.split('-')

                for (i in splitPair[0].toLong()..splitPair[1].toLong()) {
                    val stringed = i.toString()
                    if (stringed.length % 2 == 1) continue
                    if (stringed.drop(stringed.length / 2) == stringed.dropLast(stringed.length / 2)) {
                        result += i
                    }
                }

            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        var result = 0L
        File(filePath).forEachLine { line ->
            // Should only be one
            for (idPair in line.split(',').listIterator()) {
                val splitPair = idPair.split('-')
                pairLoop@ for (i in splitPair[0].toLong()..splitPair[1].toLong()) {
                    val stringed = i.toString()
                    for (patternLength in 1.. stringed.length / 2) {
                        if (stringed.chunked(patternLength).associateWith { it }.size == 1) {
                            result += i
                            continue@pairLoop
                        }
                    }
                }
            }
        }
        return result
    }
}