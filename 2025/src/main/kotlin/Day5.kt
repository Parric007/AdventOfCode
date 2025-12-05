package main

import java.io.File

class Day5: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val (unparsedRanges, unparsedIds) = File(filePath).readText().split("\n\n")
        val ranges = unparsedRanges.split("\n").map {  rangeString: String ->
            val (start, end) = rangeString.split("-").map { it.toLong() }
            start..end
        }

        return unparsedIds.split("\n").count { string ->
            ranges.any { string.toLong() in it }
        }.toLong()
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val (unparsedRanges, _) = File(filePath).readText().split("\n\n")
        val ranges = unparsedRanges.split("\n").map {  rangeString: String ->
            val (start, end) = rangeString.split("-").map { it.toLong() }
            start..end
        }.sortedBy { it.first }

        val overlapFreeList = mutableListOf<LongRange>()
        for (range in ranges) {
            if (overlapFreeList.isEmpty() || overlapFreeList.last().last < range.first - 1) {
                overlapFreeList.add(range)
            } else {
                val last = overlapFreeList.removeAt(overlapFreeList.size - 1)
                overlapFreeList.add(last.first..maxOf(last.last, range.last))
            }
        }

        return overlapFreeList.sumOf { it.last - it.first + 1 }
    }
}