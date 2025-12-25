package main

import java.io.File

class Day13: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        return solve(filePath, 0)
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return solve(filePath, 10_000_000_000_000)
    }

    private fun solve(filePath: String, prizeOffset: Long): Long {
        return File(filePath).readText().split("\n\n").sumOf {
            val nums = numberRegex.findAll(it).map { mapIt -> mapIt.value.toLong() }.toList()
            Machine(nums[0], nums[1], nums[2], nums[3], nums[4] + prizeOffset, nums[5] + prizeOffset).solve()
        }
    }

    private data class Machine(val buttonAX: Long, val buttonAY: Long,
                               val buttonBX: Long, val buttonBY: Long,
                               val prizeX: Long, val prizeY: Long,) {
        private val buttonACost = 3L
        private val buttonBCost = 1L

        fun solve(): Long {
            val det = buttonAX * buttonBY - buttonAY * buttonBX
            if (det == 0L) return 0

            val aNum = prizeX * buttonBY - prizeY * buttonBX
            val bNum = buttonAX * prizeY - buttonAY * prizeX

            if (aNum % det != 0L || bNum % det != 0L) return 0

            val a = aNum / det
            val b = bNum / det

            if (a < 0 || b < 0) return 0

            return a * buttonACost + b * buttonBCost
        }
    }

    companion object {
        private val numberRegex = Regex("""-?\d+""")
    }
}