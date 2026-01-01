package main

import java.io.File

class Day21: Day {

    private val numericKeyPad = mapOf(
        '0' to Point(1, 0),
        'A' to Point(2, 0),
        '1' to Point(0, 1),
        '2' to Point(1, 1),
        '3' to Point(2, 1),
        '4' to Point(0, 2),
        '5' to Point(1, 2),
        '6' to Point(2, 2),
        '7' to Point(0, 3),
        '8' to Point(1, 3),
        '9' to Point(2, 3),
    )

    private val directionalKeyPad = mapOf(
        '<' to Point(0, 0),
        'v' to Point(1, 0),
        '>' to Point(2, 0),
        '^' to Point(1, 1),
        'A' to Point(2, 1)
    )

    override fun processTextInputPartOne(filePath: String): Long {
        return getResult(2, File(filePath).readLines())
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return getResult(25, File(filePath).readLines())
    }

    private data class Point(val x: Int, val y: Int)

    private fun getResult(robotDepth: Int, input: List<String>): Long {
        var result = 0L

        for (code in input) {
            var currentCache = HashMap<String, Long>()

            val keySequence = getKeySequence(code, true)
            updateCache(keySequence, currentCache, 1L)

            currentCache = processCommandString(currentCache, robotDepth)
            val commandLength = getCommandLength(currentCache)

            val puzzleNumber = code.filter { it.isDigit() }.toLong()

            result += commandLength * puzzleNumber
        }

        return result
    }

    private fun getKeySequence(input: String, isNumbers: Boolean): String {
        var currentPosition = if (isNumbers) {
            numericKeyPad['A']!!
        } else {
            directionalKeyPad['A']!!
        }

        val sb = StringBuilder()

        for (c in input.toCharArray()) {
            val target = when(isNumbers) {
                true -> numericKeyPad[c]
                else -> directionalKeyPad[c]
            }!!

            var forcedOrder = ForcedOrder.NO_PREFERENCE

            if (isNumbers) {
                if (currentPosition.y == 0 && target.x == 0) {
                    forcedOrder = ForcedOrder.UD_LR
                }
                if (currentPosition.x == 0 && target.y == 0) {
                    forcedOrder = ForcedOrder.LR_UD
                }
            } else {
                if (currentPosition.x == 0) {
                    forcedOrder = ForcedOrder.LR_UD
                }
                if (target.x == 0) {
                    forcedOrder = ForcedOrder.UD_LR
                }
            }
            sb.append(getMoveSequence(target.x - currentPosition.x, target.y - currentPosition.y, forcedOrder))
            sb.append('A')
            currentPosition = target
        }
        
        return sb.toString()
    }

    private fun updateCache(commands: String, newCache: HashMap<String, Long>, qty: Long) {
        for (command in splitCommands(commands)) {
            newCache[command] = (newCache[command] ?: 0) + qty
        }
    }

    private fun splitCommands(s: String): List<String> {
        val commands = mutableListOf<String>()
        val current = StringBuilder()

        for (c in s) {
            current.append(c)
            if (c == 'A') {
                commands.add(current.toString())
                current.clear()
            }
        }

        return commands
    }

    private fun getCommandLength(currentCache: HashMap<String, Long>): Long {
        return currentCache.entries.sumOf { (key, count) ->
            key.length.toLong() * count
        }
    }

    private fun processCommandString(currentCachePassed: HashMap<String, Long>, robotDepth: Int): HashMap<String, Long> {
        var currentCache = currentCachePassed
        for (arrowPadCounter in 0 until robotDepth) {
            val newCache = HashMap<String, Long>()
            for ((commandSnip, value) in currentCache) {
                val commandString = getKeySequence(commandSnip, false)
                updateCache(commandString, newCache, value)
            }

            currentCache = newCache
        }

        return currentCache
    }

    private fun getMoveSequence(xDiff: Int, yDiff: Int, forcedOrder: ForcedOrder): String {
        val sb = StringBuilder()

        val horizontal =
            if (xDiff > 0) ">".repeat(xDiff) else "<".repeat(-xDiff)
        val vertical =
            if (yDiff > 0) "^".repeat(yDiff) else "v".repeat(-yDiff)

        when (forcedOrder) {
            ForcedOrder.NO_PREFERENCE -> {
                if (xDiff < 0) {
                    sb.append(horizontal); sb.append(vertical)
                } else {
                    sb.append(vertical); sb.append(horizontal)
                }
            }
            ForcedOrder.UD_LR -> {
                sb.append(vertical); sb.append(horizontal)
            }
            ForcedOrder.LR_UD -> {
                sb.append(horizontal); sb.append(vertical)
            }
        }
        return sb.toString()
    }

    private enum class ForcedOrder {
        NO_PREFERENCE,
        UD_LR,
        LR_UD
    }
}