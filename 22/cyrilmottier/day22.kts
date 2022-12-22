import java.io.File
import java.lang.IllegalArgumentException

val worldData = File("input.txt")
    .readLines()

val map = worldData
    .dropLast(2)
    .foldIndexed(mutableMapOf<Pair<Long, Long>, Char>()) { rowIndex, acc, row ->
        row.forEachIndexed { colIndex, cell ->
            if (cell in listOf('.', '#')) {
                acc[(rowIndex + 1L) to (colIndex + 1L)] = cell
            }
        }
        acc
    }

val instruction = worldData.last()

var row = 1L
var col = map.keys
    .filter { it.first == row }
    .minBy { it.second }
    .second

var instructionIndex = 0
var direction = 0

while (instructionIndex < instruction.length) {
    when (instruction[instructionIndex]) {
        in '0'..'9' -> {
            // Parse
            var count = 1
            while (
                instructionIndex + count < instruction.length &&
                instruction[instructionIndex + count] in '0'..'9'
            ) {
                count++
            }
            var moveBy = instruction.slice(instructionIndex..instructionIndex + count - 1).toInt()

            // Move
            while (moveBy > 0) {
                val (dRow, dCol) = when (direction) {
                    0 -> 0 to 1
                    1 -> 1 to 0
                    2 -> 0 to -1
                    3 -> -1 to 0
                    else -> throw IllegalArgumentException()
                }
                val newCell = (row + dRow) to (col + dCol)
                when (map[newCell]) {
                    '#' -> break
                    '.' -> {
                        row = newCell.first
                        col = newCell.second
                    }
                    else -> {
                        // We're out. Let's wrap around
                        when (direction) {
                            0 -> col = map.keys.filter { it.first == row }.minBy { it.second }.second
                            1 -> row = map.keys.filter { it.second == col }.minBy { it.first }.first
                            2 -> col = map.keys.filter { it.first == row }.maxBy { it.second }.second
                            3 -> row = map.keys.filter { it.second == col }.maxBy { it.first }.first
                        }
                    }
                }
                moveBy--
            }

            // Update
            instructionIndex += count
        }
        'R' -> {
            instructionIndex++
            direction = (direction + 1) % 4
        }
        'L' -> {
            instructionIndex++
            direction = (direction - 1 + 4) % 4
        }
    }
}

row  * 1000 + col * 4 + direction
