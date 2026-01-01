package main

import java.io.File

class Day22: Day {
    private val secretNumbers = mutableListOf<ByteArray>()

    override fun processTextInputPartOne(filePath: String): Long {
        val lines = File(filePath).readLines()

        var result = 0L

        secretNumbers.clear()

        for (line in lines) {
            var secretNumber = line.toLong()

            val lastDigitsList = ByteArray(STEPS)

            for (i in 0 until STEPS) {
                lastDigitsList[i] = (secretNumber % 10).toByte()
                secretNumber = generateNextSecretNumber(secretNumber)
            }

            secretNumbers.add(lastDigitsList)

            result += secretNumber
        }

        return result
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val totalBananas = LongArray(19 * 19 * 19 * 19)

        val seen = BooleanArray(totalBananas.size)

        for (buyerPrices in secretNumbers) {
            seen.fill(false)
            for (i in 0..buyerPrices.size - 5) {
                val delta0 = buyerPrices[i + 1] - buyerPrices[i]
                val delta1 = buyerPrices[i + 2] - buyerPrices[i + 1]
                val delta2 = buyerPrices[i + 3] - buyerPrices[i + 2]
                val delta3 = buyerPrices[i + 4] - buyerPrices[i + 3]
                val patternKey = encodePattern(delta0, delta1, delta2, delta3)

                if (!seen[patternKey]) {
                    totalBananas[patternKey] = totalBananas[patternKey] + buyerPrices[i + 4]
                    seen[patternKey] = true
                }
            }
        }

        return totalBananas.maxOrNull() ?: 0L
    }

    private fun generateNextSecretNumber(secretNumber: Long): Long {
        var nextSecretNumber = secretNumber

        nextSecretNumber = (nextSecretNumber xor (nextSecretNumber * 64)) and 0xFFFFFF
        nextSecretNumber = (nextSecretNumber xor (nextSecretNumber / 32)) and 0xFFFFFF
        return (nextSecretNumber xor (nextSecretNumber * 2048)) and 0xFFFFFF
    }

    private fun encodePattern(delta0: Int, delta1: Int, delta2: Int, delta3: Int): Int {
        val base = 9

        return ((delta0 + base) * 19 * 19 * 19) +
                ((delta1 + base) * 19 * 19) +
                ((delta2 + base) * 19) +
                (delta3 + base)
    }

    companion object {
        const val STEPS = 2000
    }
}