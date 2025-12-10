package main

import java.io.File

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
        val machineList = File(filePath).readLines()
        var result = 0L

        val machineRegex = Regex("""\[([.|#]*)]\s(.*)\s(\{[\d,]+})""")

        for (machineString in machineList) {
            val split = machineRegex.find(machineString)

            if (split != null) {
                val goal = split.groups[3]?.value!!
                val buttonOptions = split.groups[2]?.value!!
                val targetValues = goal.substring(1, goal.lastIndex).split(",").map { it.toInt() }
                val terms = buttonOptions.split(" ").map { s ->
                    return@map s.substring(1, s.lastIndex).split(",").map { it.toInt() }.toList()
                }

                //TODO: do linear solving with targetValues + terms

            }
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