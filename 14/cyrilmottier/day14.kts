import java.io.File
import kotlin.math.abs
import kotlin.math.sign

class Cave {

    val cells: List<MutableList<Char>>
    val sourceX: Int

    constructor(paths: List<List<Pair<Int, Int>>>, addFloor: Boolean) {

        val (maxDepth, minX, maxX) = if (addFloor) {
            val depth = paths.maxOf { path -> path.maxOf { it.second } } + 2
            listOf(
                paths.maxOf { path -> path.maxOf { it.second } } + 2,
                500 - depth,
                500 + depth
            )
        } else {
            listOf(
                paths.maxOf { path -> path.maxOf { it.second } },
                paths.minOf { path -> path.minOf { it.first } },
                paths.maxOf { path -> path.maxOf { it.first } }
            )
        }

        sourceX = 500 - minX

        cells = List(maxDepth + 1) { MutableList(maxX - minX + 1) { '.' } }

        paths.forEach { path ->
            path.zipWithNext().forEach { (start, end) ->
                val deltaX = end.first - start.first
                for (i in 0..abs(deltaX)) {
                    cells[start.second][start.first + i * deltaX.sign - minX] = '#'
                }
                val deltaDepth = end.second - start.second
                for (i in 0..abs(deltaDepth)) {
                    cells[start.second + i * deltaDepth.sign][start.first - minX] = '#'
                }
            }
        }

        if (addFloor) {
            for (i in minX..maxX) {
                cells[cells.size - 1][i - minX] = '#'
            }
        }
    }

    private fun step(): Boolean {
        var sandX = sourceX
        var sandDepth = 0
        while (true) {
            if (sandDepth >= cells.size - 1 || cells[sandDepth][sandX] == 'o') {
                return true
            }
            if (cells[sandDepth + 1][sandX] == '.') {
                sandDepth++
            } else {
                if (sandX == 0 || sandX == cells[0].size - 1) {
                    return true
                } else if (cells[sandDepth + 1][sandX - 1] == '.') {
                    sandDepth++
                    sandX--
                } else if (cells[sandDepth + 1][sandX + 1] == '.') {
                    sandDepth++
                    sandX++
                } else {
                    break;
                }
            }
        }
        cells[sandDepth][sandX] = 'o'
        return false
    }

    fun unleashTheKracken(): Int {
        var count = 0
        while (!step()) { count++ }
        return count
    }
}

val paths = File("input.txt").readLines()
    .map { path ->
        path.split(" -> ").map {
            val coords = it.split(",")
            coords[0].toInt() to coords[1].toInt()
        }
    }

println(Cave(paths, false).unleashTheKracken())
println(Cave(paths, true).unleashTheKracken())
