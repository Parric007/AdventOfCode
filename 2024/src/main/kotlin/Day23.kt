package main

import java.io.File

class Day23: Day {
    private val graph = mutableMapOf<String, MutableSet<String>>()
    private var maxCluster = ArrayList<Int>()

    override fun processTextInputPartOne(filePath: String): Long {
        graph.clear()

        val connections = File(filePath).readLines().map{ it.toConnection() }
        var result = 0L

        for ((first, second) in connections) {
            graph.getOrPut(first) { mutableSetOf() }.add(second)
            graph.getOrPut(second) { mutableSetOf() }.add(first)
        }

        for ((a, neighbors) in graph) {
            val list = neighbors.toList()
            for (i in list.indices) {
                val b = list[i]
                for (j in i + 1 until list.size) {
                    val c = list[j]
                    if (
                        graph[b]!!.contains(c) &&
                        (a.startsWith("t") || b.startsWith("t") || c.startsWith("t"))
                    ) {
                        result++
                    }
                }
            }
        }

        return result / 3
    }

    override fun processTextInputPartTwo(filePath: String): Long {
        val connections = File(filePath).readLines().map { it.toConnection() }
        val vertices = connections.flatMap { listOf(it.first, it.second) }.toSet()
        val idOf = vertices.withIndex().associate { it.value to it.index }
        // val vertexOf = vertices.withIndex().associate { it.index to it.value }

        val n = vertices.size
        val intGraph = Array(n) { mutableSetOf<Int>() }
        for ((a, b) in connections) {
            val u = idOf[a]!!
            val v = idOf[b]!!
            intGraph[u].add(v)
            intGraph[v].add(u)
        }

        val ordering = degeneracyOrder(intGraph)
        val index = ordering.withIndex().associate { it.value to it.index }

        maxCluster.clear()

        for (v in ordering) {
            val neighbors = intGraph[v]
            val candidates = neighbors.filter { index[it]!! > index[v]!! }.toMutableSet()
            val processed = neighbors.filter { index[it]!! < index[v]!! }.toMutableSet()
            val currentClique = mutableSetOf(v)
            bronKerbosch(currentClique, candidates, processed, intGraph)
        }

        // val result = maxCluster.map { vertexOf[it]!! }.sorted()
        // println(result.joinToString(","))
        return 0L
    }

    data class Connection(val first: String, val second: String)

    private fun String.toConnection(): Connection {
        val (first, second) = split("-")
        return Connection(first, second)
    }

    private fun degeneracyOrder(graph: Array<MutableSet<Int>>): List<Int> {
        val n = graph.size
        val degrees = IntArray(n) { graph[it].size }
        val visited = BooleanArray(n)
        val ordering = mutableListOf<Int>()

        repeat(n) {
            var minDeg = Int.MAX_VALUE
            var u = -1
            for (i in 0 until n) {
                if (!visited[i] && degrees[i] < minDeg) {
                    minDeg = degrees[i]
                    u = i
                }
            }
            if (u == -1) return@repeat
            visited[u] = true
            ordering.add(u)
            for (v in graph[u]) {
                if (!visited[v]) degrees[v]--
            }
        }
        return ordering
    }

    private fun bronKerbosch(currentClique: MutableSet<Int>,
                             candidates: MutableSet<Int>,
                             processed: MutableSet<Int>,
                             graph: Array<MutableSet<Int>>) {
        if (currentClique.size + candidates.size <= maxCluster.size) return

        if (candidates.isEmpty() && processed.isEmpty()) {
            if (currentClique.size > maxCluster.size) {
                maxCluster = ArrayList(currentClique)
            }
            return
        }

        val union = candidates.union(processed)
        val pivot = union.maxByOrNull { u -> graph[u].count { it in candidates } } ?: return

        val possibles = candidates.filter { it !in graph[pivot] }.toList()

        for (v in possibles) {
            currentClique.add(v)

            val neighbors = graph[v]
            val newCandidates = candidates.intersect(neighbors).toMutableSet()
            val newProcessed = processed.intersect(neighbors).toMutableSet()

            bronKerbosch(currentClique, newCandidates, newProcessed, graph)

            currentClique.remove(v)
            candidates.remove(v)
            processed.add(v)
        }
    }
}