import java.io.File

data class Point(
    val x: Int,
    val y: Int,
)

val NORTH = 1
val SOUTH = 2
val WEST = 3
val EAST = 4

val DIRECTIONS = listOf(NORTH, SOUTH, WEST, EAST)

val ADJACENT_OFFSETS = listOf(
    -1 to -1, 0 to -1, 1 to -1,
    -1 to 0, 1 to 0,
    -1 to 1, 0 to 1, 1 to 1,
)

val ADJACENT_OFFSET = mapOf(
    NORTH to listOf(-1 to -1, 0 to -1, 1 to -1),
    SOUTH to listOf(-1 to 1, 0 to 1, 1 to 1),
    WEST to listOf(-1 to -1, -1 to 0, -1 to 1),
    EAST to listOf(1 to -1, 1 to 0, 1 to 1)
)

val world = File("input.txt").readLines()
    .foldIndexed(mutableMapOf<Point, Char>()) { rowIndex, acc, row ->
        row.forEachIndexed { colIndex, cell ->
            acc[Point(colIndex, rowIndex)] = cell
        }
        acc
    }

dumpWorld()

var directionIndex = 0
var round = 0

while (round < 10) {
    // Round 1
    val proposals = world
        .filter { it.value == '#' }
        .mapNotNull { (point, _) ->
            // Can we move?
            if (ADJACENT_OFFSETS.any { (dx, dy) -> world[Point(point.x + dx, point.y + dy)] == '#' }) {
                // Try to move
                var tries = 0
                while (tries < DIRECTIONS.size) {
                    val direction = DIRECTIONS[(directionIndex + tries) % DIRECTIONS.size]
                    if (ADJACENT_OFFSET[direction]!!.all { (dx, dy) -> world[Point(point.x + dx, point.y + dy)] != '#' }) {
                        val newPoint = when (direction) {
                            NORTH -> Point(point.x, point.y - 1)
                            SOUTH -> Point(point.x, point.y + 1)
                            WEST -> Point(point.x - 1, point.y)
                            EAST -> Point(point.x + 1, point.y)
                            else -> throw IllegalArgumentException()
                        }
                        return@mapNotNull point to newPoint
                    }
                    tries++
                }
            }
            null
        }

    // Round 2
    val elvesMoving = proposals
        .groupBy { it.second }
        .filter { it.value.size == 1 }

    elvesMoving.forEach { destination, (originDestination) ->
        world[originDestination.first] = '.'
        world[destination] = '#'
    }

    // dumpWorld()

    directionIndex = (directionIndex + 1) % DIRECTIONS.size
    round++
}

fun dumpWorld() {
    val minX = world.filter { it.value == '#' }.minOf { it.key.x }
    val maxX = world.filter { it.value == '#' }.maxOf { it.key.x }
    val minY = world.filter { it.value == '#' }.minOf { it.key.y }
    val maxY = world.filter { it.value == '#' }.maxOf { it.key.y }

    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            print(world[Point(x, y)] ?: '.')
        }
        println()
    }
    println()
}

val minX = world.filter { it.value == '#' }.minOf { it.key.x }
val maxX = world.filter { it.value == '#' }.maxOf { it.key.x }
val minY = world.filter { it.value == '#' }.minOf { it.key.y }
val maxY = world.filter { it.value == '#' }.maxOf { it.key.y }

val emptyCount = (maxX - minX + 1) * (maxY - minY + 1) - world.count { it.value == '#' }

emptyCount
