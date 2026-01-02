package main

import java.io.File
import kotlin.math.pow

class Day24: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val (initialStates, rules) = File(filePath).readText().split("\n\n")

        val stateMap = mutableMapOf<String, Boolean>()
        initialStates.split("\n").forEach { line ->
            val (name, state) = line.split(":")
            stateMap[name] = state == " 1"
        }
        val listOfOperations = rules.split("\n").map { line ->
            val (left, result) = line.split(" -> ")
            val (input1, operation, input2) = left.split(" ")

            Line(input1, input2, operation, result)
        }.toMutableList()

        val zStates = runOperations(stateMap, listOfOperations).filter { (key, value) ->
            key.startsWith("z") && value }

        var result = 0L
        for (key in zStates.keys) {
            result += (2.0).pow(key.substringAfter("z").toDouble()).toLong()
        }
        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val (_, rules) = File(filePath).readText().split("\n\n")

        val listOfOperations = rules.split("\n").map { line ->
            val (left, result) = line.split(" -> ")
            val (input1, operation, input2) = left.split(" ")

            Line(input1, input2, operation, result)
        }

        val wrong = findWrongWires(listOfOperations)

        wrong.sorted().joinToString(",")

        return 0
    }

    private fun runOperations(
        initialStateMap: MutableMap<String, Boolean>,
        initialListOfOperations: MutableList<Line>): MutableMap<String, Boolean> {
        var slidingIndex = 0

        while (initialListOfOperations.size > 0) {
            var progress = false
            var i = 0
            while (i < initialListOfOperations.size) {
                val (input1, input2, operation, result) = initialListOfOperations[i]
                val stateOfInput1 = initialStateMap[input1]
                val stateOfInput2 = initialStateMap[input2]
                if (stateOfInput1 == null || stateOfInput2 == null) {
                    i++
                    continue
                }

                initialStateMap[result] = when (operation) {
                    "XOR" -> stateOfInput1 xor stateOfInput2
                    "OR" -> stateOfInput1 or stateOfInput2
                    "AND" -> stateOfInput1 and stateOfInput2
                    else -> error("What?")
                }

                initialListOfOperations.removeAt(i)
                slidingIndex++
                progress = true
            }

            if (!progress) break
        }

        return initialStateMap
    }

    private fun findWrongWires(listOfOperations: List<Line>): Set<String> {
        val wrongWires = mutableSetOf<String>()

        val highestZ = listOfOperations.maxByOrNull { it.result }?.result

        listOfOperations.forEach { (input1, input2, operation, result) ->
            if (result[0] == 'z' && operation != "XOR" && result != highestZ) wrongWires.add(result)

            val xyzSet = setOf('x', 'y', 'z')
            if (operation == "XOR" && result.first() !in xyzSet && input1.first() !in xyzSet && input2.first() !in xyzSet) {
                wrongWires.add(result)
            }
            if (operation == "AND" && "x00" !in setOf(input1, input2)) {
                listOfOperations.forEach { (subInput1, subInput2, subOperation, _) ->
                    if ((result == subInput1 || result == subInput2) && subOperation != "OR") wrongWires.add(result)
                }
            }
            if (operation == "XOR") {
                listOfOperations.forEach { (subInput1, subInput2, subOperation, _) ->
                    if ((result == subInput1 || result == subInput2) && subOperation == "OR") wrongWires.add(result)
                }
            }
        }

        return wrongWires
    }

    private data class Line(val input1: String, val input2: String, val operation: String, val result: String)
}