package main

import java.io.File

class Day12: Day {
    override fun processTextInputPartOne(filePath: String): String {
        val json = File(filePath).readText()

        var result = 0

        var i = 0
        while (i < json.length) {
            if (json[i] == '-' || json[i].isDigit()) {
                var sign = 1
                if (json[i] == '-') {
                    sign = -1
                    i++
                }

                var value = 0
                while (i < json.length && json[i].isDigit()) {
                    value = value * 10 + (json[i] - '0')
                    i++
                }

                result += sign * value
            } else {
                i++
            }
        }
        return result.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val json = File(filePath).readText()

        return Parser(json).parseValue().toString()
    }

    private class Parser(private val input: String) {
        private var i = 0

        fun parseValue(): Int {
            return when (input[i]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> {
                    parseString()
                    0
                }
                else -> parseNumber()
            }
        }

        private fun parseObject(): Int {
            i++
            var sum = 0
            var hasRed = false

            while (input[i] != '}') {
                parseString()
                i++

                if (input[i] == '"') {
                    val value = parseString()
                    if (value == "red") hasRed = true
                } else {
                    sum += parseValue()
                }

                if (input[i] == ',') i++
            }

            i++
            return if (hasRed) 0 else sum
        }

        private fun parseArray(): Int {
            i++
            var sum = 0

            while (input[i] != ']') {
                sum += parseValue()
                if (input[i] == ',') i++
            }

            i++
            return sum
        }

        private fun parseString(): String {
            i++
            val start = i

            while (input[i] != '"') i++

            val result = input.substring(start, i)
            i++
            return result
        }

        private fun parseNumber(): Int {
            val start = i
            if (input[i] == '-') i++

            while (i < input.length && input[i].isDigit()) i++

            return input.substring(start, i).toInt()
        }
    }
}