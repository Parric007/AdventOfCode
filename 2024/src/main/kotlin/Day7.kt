package main

import java.io.File

class Day7 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        return getEquations(filePath).sumOf {it.isTruePart1() }
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return getEquations(filePath).sumOf { it.isTruePart2() }
    }

    private fun getEquations(filePath: String): List<Equation> {
        return File(filePath).readLines().map { line ->
            val (result, terms) = line.split(":")
            val longArray = terms.trim().split(" ").map { it.toLong()}.toLongArray()

            return@map Equation(result.toLong(), longArray)
        }
    }

    data class Equation(
        val result: Long,
        val terms: LongArray,
    ) {
        fun isTruePart1(): Long {
            fun dfs(index: Int, current: Long): Boolean {
                if (index == terms.size) {
                    return current == result
                }
                val num = terms[index]
                if (dfs(index + 1, current + num)) return true
                if (dfs(index + 1, current * num)) return true

                return false
            }

            return if (dfs(1, terms[0])) result
            else 0
        }

        fun isTruePart2(): Long {
            fun dfs(index: Int, current: Long): Boolean {
                if (index == terms.size) {
                    return current == result
                }
                val next = terms[index]
                if (dfs(index + 1, concat(current, next))) return true
                if (dfs(index + 1, current + next)) return true
                if (dfs(index + 1, current * next)) return true
                return false
            }

            return if (dfs(1, terms[0])) result
            else 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Equation

            if (result != other.result) return false
            if (!terms.contentEquals(other.terms)) return false

            return true
        }

        override fun hashCode(): Int {
            var result1 = result.hashCode()
            result1 = 31 * result1 + terms.contentHashCode()
            return result1
        }

        private fun concat(a: Long, b: Long): Long {
            var factor = 1L
            var temp = b
            while (temp > 0) {
                factor *= 10
                temp /= 10
            }
            return a * factor + b
        }
    }

}