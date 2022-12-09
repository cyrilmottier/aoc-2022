import java.io.File
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sign

data class State(
    val visitedCells: MutableSet<Pair<Int, Int>> = mutableSetOf(0 to 0),
    var head: Pair<Int, Int> = 0 to 0,
    var tail: Pair<Int, Int> = 0 to 0
)

fun sign(value: Int) = when {
    value == 0 -> 0
    value > 0 -> 1
    else -> -1
}

File("input.txt")
    .readLines()
    .fold(State()) { state, command ->
        val (headVx, headVy) = when (command[0]) {
            'R' -> 1 to 0
            'L' -> -1 to 0
            'D' -> 0 to 1
            'U' -> 0 to -1
            else -> throw IllegalArgumentException()
        }

        repeat(command.drop(2).toInt()) {
            with(state) {
                head = (head.first + headVx) to (head.second + headVy)

                val distX = head.first - tail.first
                val distY = head.second - tail.second

                if (abs(distX) > 1 || abs(distY) > 1) {
                    val tailVx = sign(distX)
                    val tailVy = sign(distY)

                    tail = (tail.first + tailVx) to (tail.second + tailVy)
                    visitedCells += tail
                }
            }
        }

        state
    }
    .visitedCells.size
