package main

import java.io.File

class Day7: Day {
    private var valueOfA: UShort = 0u

    override fun processTextInputPartOne(filePath: String): String {
        val actions = File(filePath).readLines().map { it.toAction() } .toMutableList()

        valueOfA = runActions(actions, mutableMapOf())["a"]!!

        return valueOfA.toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val actions = File(filePath).readLines().map { it.toAction() }.toMutableList()

        return runActions(actions, mutableMapOf("b" to valueOfA))["a"].toString()
    }

    private fun String.toAction(): Action {
        val (left, result) = this.split(" -> ")

        val splitLeft = left.split(" ")
        return when (splitLeft.size) {
            1 -> Action("WRITE", left, "", result)
            2 -> Action("NOT", splitLeft[1], "", result)
            else -> Action(splitLeft[1], splitLeft[0], splitLeft[2],result)
        }
    }

    private data class Action(val operator: String, val left: String, val right: String, val result: String)

    private fun runActions(actions: MutableList<Action>, wireStates: MutableMap<String, UShort>): MutableMap<String, UShort> {
        while (actions.size > 0) {
            for (i in actions.indices.reversed()) {
                val action = actions[i]
                when (action.operator) {
                    "WRITE" -> {
                        val toWrite = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        wireStates[action.result] = toWrite
                    }
                    "AND" -> {
                        val leftVal = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        val rightVal = action.right.toUShortOrNull() ?: wireStates[action.right] ?: continue

                        wireStates[action.result] = leftVal and rightVal
                    }
                    "OR" -> {
                        val leftVal = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        val rightVal = action.right.toUShortOrNull() ?: wireStates[action.right] ?: continue

                        wireStates[action.result] = leftVal or rightVal
                    }
                    "NOT" -> {
                        val toNot = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        wireStates[action.result] = toNot.inv()
                    }
                    "LSHIFT" -> {
                        val leftVal = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        val rightVal = action.right.toInt()

                        wireStates[action.result] = (leftVal.toInt() shl rightVal).toUShort()
                    }
                    "RSHIFT" -> {
                        val leftVal = action.left.toUShortOrNull() ?: wireStates[action.left] ?: continue
                        val rightVal = action.right.toInt()

                        wireStates[action.result] = (leftVal.toInt() ushr rightVal).toUShort()
                    }
                }
                actions.removeAt(i)
            }
        }

        return wireStates
    }
}