package main

import java.io.File
import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPConstraint
import com.google.ortools.linearsolver.MPSolver

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
                val targetValues = goal.substring(1, goal.lastIndex).split(",").map { it.toInt() }.toIntArray()
                val terms = buttonOptions.split(" ").map { s ->
                    return@map s.substring(1, s.lastIndex).split(",").map { it.toInt() }.toList()
                }

                val listForMatrices = List(terms.size) {IntArray(targetValues.size)}

                terms.forEachIndexed { i, term ->
                    term.forEach { j ->
                        listForMatrices[i][j] = 1
                    }
                }
                val solutions = solveMinSumInteger(transpose(listForMatrices), targetValues)

                if (solutions != null) {
                    result += solutions.sum()
                }
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

    private fun transpose(list: List<IntArray>): List<IntArray> {
        val rows = list.size
        val cols = list[0].size
        return List(cols) { c ->
            IntArray(rows) { r -> list[r][c] }
        }
    }

    private fun solveMinSumInteger(
        listForMatrices: List<IntArray>,
        targetValues: IntArray
    ): LongArray? {
        Loader.loadNativeLibraries()

        val m = listForMatrices.size
        val n = listForMatrices[0].size
        val solver = MPSolver.createSolver("CBC_MIXED_INTEGER_PROGRAMMING")
            ?: throw RuntimeException("Could not create solver (native libraries missing?)")

        val upperBound = 1_000.0  // adjust if you have a tighter bound
        val vars = Array(n) { i ->
            solver.makeIntVar(0.0, upperBound, "n_$i")
        }

        for (r in 0 until m) {
            val coefficients = listForMatrices[r]
            val constraint: MPConstraint = solver.makeConstraint(targetValues[r].toDouble(), targetValues[r].toDouble(), "eq_$r")
            for (j in 0 until n) {
                val a = coefficients[j].toDouble()
                if (a != 0.0) constraint.setCoefficient(vars[j], a)
            }
        }
        val objective = solver.objective()
        for (j in 0 until n) objective.setCoefficient(vars[j], 1.0)
        objective.setMinimization()

        val resultStatus = solver.solve()

        return if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
            LongArray(n) { j -> Math.round(vars[j].solutionValue()) }
        } else {
            null
        }
    }
}