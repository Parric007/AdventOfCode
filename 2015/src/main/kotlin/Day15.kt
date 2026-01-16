package main

import java.io.File
import kotlin.math.max

class Day15: Day {

    private val teaspoons = 100

    override fun processTextInputPartOne(filePath: String): String {
        val ingredients = File(filePath).readLines().map { it.toIngredient() }
        return findBestCombination(ingredients, null).toString()
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val ingredients = File(filePath).readLines().map { it.toIngredient() }
        return findBestCombination(ingredients, 500).toString()
    }

    private fun String.toIngredient(): Ingredient {
        val split = this.split(" ")
        return Ingredient(split[0].drop(1),
            split[2].dropLast(1).toInt(),
            split[4].dropLast(1).toInt(),
            split[6].dropLast(1).toInt(),
            split[8].dropLast(1).toInt(),
            split[10].toInt()
        )
    }

    private data class Ingredient(
        val name: String,
        val cap: Int,
        val dur: Int,
        val fla: Int,
        val tex: Int,
        val cal: Int // not needed for part1
    )

    private data class MaxProps(val cap: Int, val dur: Int, val fla: Int, val tex: Int)

    private fun computeMaxPerIngredient(ingredients: List<Ingredient>): MaxProps {
        return MaxProps(
            cap = ingredients.maxOf { it.cap },
            dur = ingredients.maxOf { it.dur },
            fla = ingredients.maxOf { it.fla },
            tex = ingredients.maxOf { it.tex }
        )
    }

    private fun findBestCombination(ingredients: List<Ingredient>, targetCalories: Int?): Int {
        val n = ingredients.size
        val bestCounts = IntArray(n)
        var bestScore = 0

        val maxProps = computeMaxPerIngredient(ingredients)

        val calorieConstraint = CalorieConstraint(targetCalories, ingredients)

        fun backtrack(
            index: Int,
            remaining: Int,
            current: IntArray,
            cap: Int,
            dur: Int,
            fla: Int,
            tex: Int,
            calories: Int
        ) {
            if (calorieConstraint.isImpossible(calories, remaining)) return

            val maxCap = cap + remaining * maxProps.cap
            val maxDur = dur + remaining * maxProps.dur
            val maxFla = fla + remaining * maxProps.fla
            val maxTex = tex + remaining * maxProps.tex

            if (maxCap <= 0 || maxDur <= 0 || maxFla <= 0 || maxTex <= 0) return

            if (index == n - 1) {
                current[index] = remaining

                val finalCalories = calories + ingredients[index].cal * remaining
                if (!calorieConstraint.isValidFinal(finalCalories)) return

                val finalCap = max(cap + ingredients[index].cap * remaining, 0)
                val finalDur = max(dur + ingredients[index].dur * remaining, 0)
                val finalFla = max(fla + ingredients[index].fla * remaining, 0)
                val finalTex = max(tex + ingredients[index].tex * remaining, 0)

                val score = finalCap * finalDur * finalFla * finalTex
                if (score > bestScore) {
                    bestScore = score
                    current.copyInto(bestCounts)
                }
                return
            }

            for (i in 0..remaining) {
                current[index] = i
                backtrack(
                    index + 1,
                    remaining - i,
                    current,
                    cap + ingredients[index].cap * i,
                    dur + ingredients[index].dur * i,
                    fla + ingredients[index].fla * i,
                    tex + ingredients[index].tex * i,
                    calories + ingredients[index].cal * i
                )
            }
        }

        backtrack(0, teaspoons, IntArray(n), 0, 0, 0, 0, 0)
        return bestScore
    }


    private class CalorieConstraint(private val target: Int?, ingredients: List<Ingredient>) {
        private val minCal = ingredients.minOf { it.cal }
        private val maxCal = ingredients.maxOf { it.cal }

        fun isImpossible(current: Int, remaining: Int): Boolean {
            if (target == null) return false

            if (current > target) return true
            if (current + remaining * maxCal < target) return true
            if (current + remaining * minCal > target) return true

            return false
        }

        fun isValidFinal(calories: Int): Boolean {
            return target == null || calories == target
        }
    }
}