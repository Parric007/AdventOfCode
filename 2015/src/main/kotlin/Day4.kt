package main

import java.io.File
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

class Day4: Day {

    private val mdPart1 = MessageDigest.getInstance("MD5")

    override fun processTextInputPartOne(filePath: String): String {
        val baseCode = File(filePath).readText().toByteArray()
        var result = 0
        val intBuffer = ByteArray(10)

        while(true) {
            mdPart1.reset()
            mdPart1.update(baseCode)

            val start = writeIntAscii(result, intBuffer)
            mdPart1.update(intBuffer, start, intBuffer.size - start)

            val digest = mdPart1.digest()

            if (digest[0] == 0.toByte() &&
                digest[1] == 0.toByte() &&
                (digest[2].toInt() and 0xF0) == 0) return result.toString()

            result++
        }
    }

    override fun processTextInputPartTwo(filePath: String): String {
        val baseCode = File(filePath).readText().toByteArray()

        val threads = Runtime.getRuntime().availableProcessors()
        val found = AtomicInteger(Int.MAX_VALUE)
        val workers = ArrayList<Thread>(threads)

        repeat(threads) { id ->
            val t = Thread {
                val md = MessageDigest.getInstance("MD5")
                val intBuffer = ByteArray(10)

                var i = id

                while(i < found.get()) {
                    md.reset()
                    md.update(baseCode)

                    val start = writeIntAscii(i, intBuffer)
                    md.update(intBuffer, start, intBuffer.size - start)

                    val digest = md.digest()

                    if (digest[0] == 0.toByte() &&
                        digest[1] == 0.toByte() &&
                        digest[2] == 0.toByte()) {
                        found.getAndUpdate { min(it, i) }
                        return@Thread
                    }
                    i += threads
                }
            }
            workers += t
            t.start()
        }

        workers.forEach { it.join() }
        return found.get().toString()
    }

    private fun writeIntAscii(value: Int, buffer: ByteArray): Int {
        var v = value
        var i = buffer.size

        do {
            buffer[--i] = ('0'.code + (v % 10)).toByte()
            v /= 10
        } while (v > 0)

        return i
    }
}