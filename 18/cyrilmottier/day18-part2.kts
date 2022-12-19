import java.io.File

val DIRS = listOf(
    listOf(1, 0, 0),
    listOf(0, 1, 0),
    listOf(0, 0, 1),
    listOf(-1, 0, 0),
    listOf(0, -1, 0),
    listOf(0, 0, -1),
)

val CUBES = File("input.txt").readLines()
    .map {
        it.split(",").map { it.toInt() }
    }

fun bounds(cubes: List<List<Int>>, dimension: Int) = cubes.minOf { it[dimension] - 1 }..cubes.maxOf { it[dimension] + 1 }

val bounds = listOf(
    bounds(CUBES, 0),
    bounds(CUBES, 1),
    bounds(CUBES, 2),
)

val queue = mutableListOf<List<Int>>(bounds.map { it.first })
val visited = mutableSetOf<List<Int>>()
var count = 0

while (queue.isNotEmpty()) {
    val item = queue.removeFirst()
    if (item in visited) continue
    visited.add(item)
    DIRS.forEach { dir ->
        val adjacentCube = item.zip(dir).map { it.first + it.second }
        if (adjacentCube in CUBES) {
            count++
        } else if (adjacentCube.zip(bounds).all { it.first in it.second }) {
            queue.add(adjacentCube)
        }
    }
}

count
