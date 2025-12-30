package main

import java.io.File

class Day17: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val lines = File(filePath).readLines()

        val registerA = lines[0].substringAfter(": ").toInt()
        val registerB = lines[1].substringAfter(": ").toInt()
        val registerC = lines[2].substringAfter(": ").toInt()

        val instructions = lines[4].substringAfter(": ").split(",").map { it.toInt() }

        val result = runProgram(registerA, registerB, registerC, instructions)
        return result.joinToString("").toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val lines = File(filePath).readLines()

        val registerB = lines[1].substringAfter(": ").toInt()
        val registerC = lines[2].substringAfter(": ").toInt()

        val instructions = lines[4].substringAfter(": ").split(",").map { it.toInt() }

        val shift = instructions.chunked(2).first { (op, operand) -> op == 0 && operand in 0..3 }[1]

        val inputBits = shift + 7
        val outputCache = IntArray(1 shl inputBits)

        for (a in outputCache.indices) {
            outputCache[a] = runProgram(a, registerB, registerC, instructions, maxOutputs = 1)[0]
        }

        return reconstructA(instructions, outputCache, shift, inputBits)
    }

    private fun runProgram(initValA: Int, initValB: Int, initValC: Int, instructions: List<Int>, maxOutputs: Int? = null): List<Int> {
        var registerA = initValA
        var registerB = initValB
        var registerC = initValC

        var instructionPointer = 0

        val output = mutableListOf<Int>()

        fun getComboOperand(instruction: Int): Int {
            return when (instruction) {
                4 -> registerA
                5 -> registerB
                6 -> registerC
                7 -> error("Not valid operand")
                else -> instruction
            }
        }

        while (true) {
            if (instructionPointer > instructions.lastIndex) break
            val opcode = instructions[instructionPointer]
            val operand = instructions[instructionPointer + 1]

            when (opcode) {
                0 -> {
                    registerA = registerA.shr(getComboOperand(operand))
                }

                1 -> {
                    registerB = registerB xor operand
                }

                2 -> {
                    registerB = getComboOperand(operand).mod(8)
                }

                3 -> {
                    if (registerA != 0) {
                        instructionPointer = operand
                        continue
                    }
                }
                4 -> {
                    registerB = registerB xor registerC
                }
                5 -> {
                    output.add(getComboOperand(operand) and 7)
                    if (maxOutputs != null && output.size >= maxOutputs) {
                        return output
                    }
                }
                6 -> {
                    registerB = registerA shr getComboOperand(operand)
                }

                7 -> {
                    registerC = registerA shr getComboOperand(operand)
                }
            }
            instructionPointer += 2
        }
        return output
    }

    private fun reconstructA(
        program: List<Int>,
        outputCache: IntArray,
        shift: Int,
        inputBits: Int
    ): Long {
        fun dfs(a: Long, pos: Int): Long? {
            if (pos == program.size) {
                if (a shr (program.size * shift) == 0L) {
                    return a
                }
                return null
            }

            for (bits in 0 until (1 shl shift)) {
                val candidate =
                    a or (bits.toLong() shl (inputBits + (pos - 1) * shift))
                val input = (candidate shr (pos * shift)).toInt()

                if (outputCache[input] == program[pos]) {
                    val result = dfs(candidate, pos + 1)
                    if (result != null) return result
                }
            }
            return null
        }

        for (a in outputCache.indices) {
            if (outputCache[a] == program[0]) {
                dfs(a.toLong(), 1)?.let { return it }
            }
        }

        error("No valid A found")
    }
}