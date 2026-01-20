package main

import java.io.File

class Day16: Day {

    override fun processTextInputPartOne(filePath: String): String {
        File(filePath).useLines { lines ->
            lines.forEach { line ->
                if (line.toSue().matchesPart1()) return line.substringBefore(":").substringAfter(": ")
            }
        }
        return ""
    }

    override fun processTextInputPartTwo(filePath: String): String {
        File(filePath).useLines { lines ->
            lines.forEach { line ->
                if (line.toSue().matchesPart2()) return line.substringBefore(":").substringAfter(": ")
            }
        }
        return ""
    }

    private companion object {
        val targetSue = Sue(
            children = 3,
            cats = 7,
            samoyeds = 2,
            pomeranians = 3,
            akitas = 0,
            vizslas = 0,
            goldfish = 5,
            trees = 3,
            cars = 2,
            perfumes = 1
        )
    }

    private data class Sue(val children: Int? = null,
                           val cats: Int? = null,
                           val samoyeds: Int? = null,
                           val pomeranians: Int? = null,
                           val akitas: Int? = null,
                           val vizslas: Int? = null,
                           val goldfish: Int? = null,
                           val trees: Int? = null,
                           val cars: Int? = null,
                           val perfumes: Int? = null
    ) {
        fun matchesPart1(): Boolean = listOf(
            children to targetSue.children,
            cats to targetSue.cats,
            samoyeds to targetSue.samoyeds,
            pomeranians to targetSue.pomeranians,
            akitas to targetSue.akitas,
            vizslas to targetSue.vizslas,
            goldfish to targetSue.goldfish,
            trees to targetSue.trees,
            cars to targetSue.cars,
            perfumes to targetSue.perfumes
        ).all { (expected, actual) ->
            expected == null || expected == actual
        }

        fun matchesPart2(): Boolean = listOf(
            Triple(children, targetSue.children) { e: Int, a: Int -> e == a },
            Triple(cats, targetSue.cats) { e: Int, a: Int -> e > a },
            Triple(samoyeds, targetSue.samoyeds) { e, a -> e == a },
            Triple(pomeranians, targetSue.pomeranians) { e, a -> e < a },
            Triple(akitas, targetSue.akitas) { e, a -> e == a },
            Triple(vizslas, targetSue.vizslas) { e, a -> e == a },
            Triple(goldfish, targetSue.goldfish) { e, a -> e < a },
            Triple(trees, targetSue.trees) { e, a -> e > a },
            Triple(cars, targetSue.cars) { e, a -> e == a },
            Triple(perfumes, targetSue.perfumes) { e, a -> e == a }
        ).all { (thisSue, targetSue, rule) ->
            thisSue == null || rule(thisSue, targetSue!!)
        }
    }

    private fun String.toSue(): Sue {
        val atr = this.substringAfter(": ").split(", ").associate {
            val (key, value) = it.split(":")
            key.trim() to value.trim().toInt()
        }

        return Sue(
            atr["children"],
            atr["cats"],
            atr["samoyeds"],
            atr["pomeranians"],
            atr["akitas"],
            atr["vizslas"],
            atr["goldfish"],
            atr["trees"],
            atr["cars"],
            atr["perfumes"]
        )
    }
}