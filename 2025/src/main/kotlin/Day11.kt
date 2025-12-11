package main

import java.io.File

class Day11: Day {
    override fun processTextInputPartOne(filePath: String): Long {
        return findAllPaths("you", getMap(filePath))
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        return findAllPathsWithParts("svr", getMap(filePath), foundFFT = false, foundDAC = false)
    }

    private fun getMap(filePath: String): MutableMap<String,List<String>> {
        val toReturn = mutableMapOf<String, List<String>>()
        File(filePath).readLines().forEach { line ->
            val split1 = line.split(":")
            toReturn[split1[0]] = split1[1].trim().split(" ")
        }
        return toReturn
    }

    private fun findAllPaths(name: String, map: MutableMap<String, List<String>>): Long {
        return if (name == "out") {
            1L
        } else map[name]!!.sumOf { findAllPaths(it, map) }
    }

    private val memo = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

    private fun findAllPathsWithParts(
        name: String,
        map: Map<String, List<String>>,
        foundFFT: Boolean,
        foundDAC: Boolean
    ): Long {
        val key = Triple(name, foundFFT, foundDAC)
        memo[key]?.let { return it }

        val result = if (name == "out") {
            if (foundFFT && foundDAC) 1L else 0L
        } else {
            val nextFoundFFT = name == "fft" || foundFFT
            val nextFoundDAC = name == "dac" || foundDAC
            map[name]!!.sumOf { findAllPathsWithParts(it, map, nextFoundFFT, nextFoundDAC) }
        }
        memo[key] = result
        return result
    }

}