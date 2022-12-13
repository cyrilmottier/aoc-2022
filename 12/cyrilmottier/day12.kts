import java.io.File

fun dijkstra(
    graph: List<String>,
    startIndex: Int,
    endIndex: Int
): Int {

    val width = graph[0].length
    val height = graph.size

    val prev = IntArray(width * height)
    val dist = IntArray(width * height) { Int.MAX_VALUE }
    dist[startIndex] = 0

    val queue = mutableSetOf<Int>().apply {
        repeat(width * height) { add(it) }
    }

    fun test(index: Int, row: Int, col: Int, rowV: Int, colV: Int) {
        val adjacentRow = row + rowV
        val adjacentCol = col + colV
        if (
            adjacentRow >= 0 &&
            adjacentRow < height &&
            adjacentCol >= 0 &&
            adjacentCol < width
        ) {
            if (graph[adjacentRow][adjacentCol] - graph[row][col] <= 1) {
                val alt = dist[index] + 1
                val adjacentIndex = adjacentRow * width + adjacentCol
                if (alt < dist[adjacentIndex]) {
                    dist[adjacentIndex] = alt
                    prev[adjacentIndex] = index
                }
            }
        }
    }

    while (queue.isNotEmpty()) {
        var minDist = Int.MAX_VALUE
        var minIndex = queue.first()
        queue.forEach {
            if (dist[it] < minDist) {
                minDist = dist[it]
                minIndex = it
            }
        }

        if (minDist == Int.MAX_VALUE) {
            return Int.MAX_VALUE
        }

        queue.remove(minIndex)

        if (minIndex == endIndex) {
            var count = 1
            var index = endIndex
            while (prev[index] != startIndex) {
                index = prev[index]
                count++
            }
            return count
        }

        val col = minIndex % width
        val row = minIndex / width

        test(minIndex, row, col, -1, 0)
        test(minIndex, row, col, 1, 0)
        test(minIndex, row, col, 0, -1)
        test(minIndex, row, col, 0, 1)
    }

    return Int.MAX_VALUE
}

val data = File("input.txt")
    .readLines()

val flatData = data.flatMap { it.toList() }
val endIndex = flatData.indexOf('E')

val world = data.map {
    it
        .replace('S', 'a')
        .replace('E', 'z')
}

fun part1() {
    val startIndex = flatData.indexOf('S')
    println(dijkstra(world, startIndex, endIndex))
}

fun part2() {
    val startIndices = flatData
        .withIndex()
        .filter { it.value == 'a' }
        .map { it.index }

    val min = startIndices
        .mapIndexed { index, it ->
            println("$index / ${startIndices.count()}: $it")
            dijkstra(world, it, endIndex)
        }
        .min()

    println(min)
}

part1()
part2()


