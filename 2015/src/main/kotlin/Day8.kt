package main

import java.io.File

class Day8: Day {
    override fun processTextInputPartOne(filePath: String): String {
        var result = 0

        File(filePath).forEachLine { line ->
            val codeLength = line.length
            var i = 1
            val sb = StringBuilder()

            while (i < line.length - 1) {
                when (line[i]) {
                    '\\' -> {
                        when (line.getOrNull(i + 1)) {
                            '\\', '"' -> {
                                sb.append('.')
                                i += 2
                            }
                            'x' -> {
                                if (i + 3 < line.length) {
                                    val hex = line.substring(i + 2, i + 4)
                                    sb.append(hex.toInt(16).toChar())
                                    i += 4
                                }
                            }
                        }
                    }
                    else -> {
                        sb.append(line[i])
                        i++
                    }
                }
            }

            result += codeLength - sb.length
        }

        return result.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        var result = 0

        File(filePath).forEachLine { line ->
            val codeLength = line.length
            var i = 0
            val sb = StringBuilder()

            while (i < line.length) {
                when (line[i]) {
                    '"' -> {
                        sb.append("\\\"")
                    }
                    '\\' -> {
                        sb.append("\\\\")
                    }
                    else -> {
                        sb.append(line[i])
                    }
                }
                i++
            }

            result += sb.length + 2 - codeLength
        }

        return result.toString()
    }
}