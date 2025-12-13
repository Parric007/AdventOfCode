package main

import java.io.File
import kotlin.math.min

class Day10: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val machineList = File(filePath).readLines()
        var result = 0L
        val machineRegex = Regex("""\[([.|#]*)]\s(.*)\s(\{[\d,]+})""")

        for (machineString in machineList) {
            val split = machineRegex.find(machineString)

            if (split != null) {
                val goal = split.groups[1]?.value!!
                val buttonOptions = split.groups[2]?.value!!
                val machine = Machine(goal, buttonOptions)
                val solved = machine.solve()
                result += solved
            }
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val lines = File(filePath).readLines().map { it.trim() }
        var result = 0L

        for (line in lines) {
            val parts = line.split(" ")
            val rawCoefficients = parts.drop(1).dropLast(1)
            val rawGoal = parts.last()
            val goal = rawGoal.substring(1, rawGoal.length - 1).split(",").map { it.toInt() }
            val nJoltages = goal.size
            val buttons: List<List<Int>> = rawCoefficients.map { r ->
                r.substring(1, r.length - 1).split(",").map { it.toInt() }
            }
            val nButtons = buttons.size

            val coefficients: List<IntArray> = buttons.map { b ->
                IntArray(nJoltages) { idx -> if (idx in b) 1 else 0 }
            }
            val patternCosts = mutableMapOf<List<Int>, MutableMap<IntArray, Int>>()
            val totalCombinations = 1 shl nButtons
            for (mask in 0 until totalCombinations) {
                var cost = 0
                val pattern = IntArray(nJoltages) { 0 }
                for (btn in 0 until nButtons) {
                    if ((mask shr btn) and 1 == 1) {
                        cost++
                        for (i in 0 until nJoltages) {
                            pattern[i] += coefficients[btn][i]
                        }
                    }
                }
                val parity = pattern.map { it % 2 }
                patternCosts.computeIfAbsent(parity) { mutableMapOf() }.putIfAbsent(pattern, cost)
            }

            data class GoalKey(val arr: IntArray) {
                override fun equals(other: Any?) = other is GoalKey && arr.contentEquals(other.arr)
                override fun hashCode() = arr.contentHashCode()
            }

            val memo = mutableMapOf<GoalKey, Long>()

            fun solve(goalState: IntArray): Long {
                val key = GoalKey(goalState)
                memo[key]?.let { return it }
                if (goalState.all { it == 0 }) return 0L

                val parityKey = goalState.map { it % 2 }
                var answer = 1_000_000_000L

                for ((pattern, patternCost) in patternCosts[parityKey] ?: emptyMap()) {
                    if (pattern.indices.all { i -> pattern[i] <= goalState[i] }) {
                        val newGoal = IntArray(goalState.size) { i -> (goalState[i] - pattern[i]) / 2 }
                        answer = min(answer, patternCost.toLong() + 2L * solve(newGoal))
                    }
                }

                memo[key] = answer
                return answer
            }

            result += solve(goal.toIntArray())
        }
        return result
    }

    class Machine() {
        private var goalState = 0
        private lateinit var possibleButtons: IntArray

        constructor(lightsGoal: String, buttonOptions: String) : this() {
            goalState = lightsGoal.replace('#', '1').replace('.', '0').toInt(2)
            val width = lightsGoal.length
            val split = buttonOptions.split(" ")
            possibleButtons = IntArray(split.size)
            split.forEachIndexed { index, s ->
                val numbers = s.substring(1, s.lastIndex)
                val chars = CharArray(width) { '0' }

                numbers.split(",").forEach { num ->
                    chars[num.toInt()] = '1'
                }
                possibleButtons[index] = chars.concatToString().toInt(2)
            }
        }

        private fun combinations(list: IntArray, k: Int): List<IntArray> {
            val result = mutableListOf<IntArray>()
            val combo = IntArray(k)
            fun backtrack(start: Int, depth: Int) {
                if (depth == k) {
                    result.add(combo.clone())
                    return
                }
                for (i in start until list.size - (k - depth) + 1) {
                    combo[depth] = list[i]
                    backtrack(i + 1, depth + 1)
                }
            }
            if (k <= list.size) backtrack(0, 0)
            return result
        }

        fun solve(): Long {
            for (i in 1 .. possibleButtons.size) {
                val combinationToCheck = combinations(possibleButtons, i)
                for (combo in combinationToCheck) {
                    if (applyMask(combo)) {
                        return i.toLong()
                    }
                }
            }
            return 0
        }

        private fun applyMask(allButtons: IntArray): Boolean {
            var currentMask = 0
            for (button in allButtons) {
                currentMask = currentMask xor button
            }
            return currentMask == goalState
        }
    }
}