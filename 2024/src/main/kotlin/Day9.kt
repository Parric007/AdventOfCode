package main

import java.io.File

class Day9 : Day {
    override fun processTextInputPartOne(filePath: String): Long {
        val fileString = File(filePath).readText()
        val disk = mutableListOf<Int>()

        fileString.forEachIndexed { index, c ->
            val length = c.digitToInt()
            if (index % 2 == 0) {
                val fileId = index / 2
                repeat(length) { disk.add(fileId) }
            } else {
                repeat(length) { disk.add(-1) }
            }
        }

        var left = 0
        var right = disk.lastIndex

        while (left < right) {
            while (left < disk.size && disk[left] != -1) left++
            while (right >= 0 && disk[right] == -1) right--

            if (left < right) {
                disk[left] = disk[right]
                disk[right] = -1
                left++
                right--
            }
        }

        var checkSum = 0L
        disk.forEachIndexed { index, i ->
            if (i != -1) {
                checkSum += i * index
            }
        }

        return checkSum
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val fileString = File(filePath).readText()
        val disk = mutableListOf<Segment>()

        fileString.forEachIndexed { index, c ->
            val length = c.digitToInt()
            if (index % 2 == 0) {
                val fileId = index / 2
                disk.add(Segment.File(fileId, length))
            } else {
                disk.add(Segment.Free(length))
            }
        }

        var right = disk.lastIndex

        while(right > 0) {
            while(disk[right] !is Segment.File) right--
            val currentFileBlock = disk[right]

            if (currentFileBlock !is Segment.File) continue

            for (i in 0 until right) {
                val toCheck = disk[i]
                if (toCheck !is Segment.Free) continue

                val sizeDiff = toCheck.size - currentFileBlock.size
                if (sizeDiff < 0) continue

                // We can move the block
                if (sizeDiff == 0) {
                    // 1:1
                    disk[right] = toCheck
                    disk[i] = currentFileBlock
                } else {
                    disk[i] = currentFileBlock
                    disk.add(i + 1, Segment.Free(sizeDiff))
                    right++

                    var newFreeSize = currentFileBlock.size
                    //Check for possibilities of merge
                    val before = disk[right - 1]
                    val after = if (right + 1 < disk.size) disk[right + 1] else null
                    if (after is Segment.Free) {
                        newFreeSize += after.size
                        disk.removeAt(right + 1)
                    }
                    if (before is Segment.Free) {
                        newFreeSize += before.size
                        disk[right - 1] = Segment.Free(newFreeSize)
                        disk.removeAt(right)
                    } else {
                        disk[right] = Segment.Free(newFreeSize)
                    }
                }
                break
            }
            right--
        }

        var checksum = 0L
        var currentIndex = 0
        for (seg in disk) {
            when (seg) {
                is Segment.File -> repeat(seg.size) { checksum += currentIndex++ * seg.id }
                is Segment.Free -> currentIndex += seg.size
            }
        }

        return checksum
    }

    sealed class Segment {
        data class File(val id: Int, val size: Int) : Segment()
        data class Free(val size: Int) : Segment()
    }
}