import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class State(
    val ropeLength: Int
) {

    private var visitedCells: MutableSet<Pair<Int, Int>> = mutableSetOf(0 to 0)
    private var rope: MutableList<Pair<Int, Int>> = MutableList(ropeLength + 1) { 0 to 0 }

    val visitedCellsCount get() = visitedCells.size

    fun moveHead(headVx: Int, headVy: Int) {
        rope[0] = (rope[0].first + headVx) to (rope[0].second + headVy)

        for (i in 1 until ropeLength + 1) {
            val distX = rope[i - 1].first - rope[i].first
            val distY = rope[i - 1].second - rope[i].second

            if (abs(distX) > 1 || abs(distY) > 1) {
                val vx = sign(distX)
                val vy = sign(distY)
                rope[i] = (rope[i].first + vx) to (rope[i].second + vy)
            }
        }
        visitedCells += rope[ropeLength]
    }

    override fun toString(): String {
        val (minX, maxX, minY, maxY) = (visitedCells + rope).fold(
            listOf(Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, Int.MIN_VALUE)
        ) { (minX, maxX, minY, maxY), item ->
            listOf(
                min(item.first, minX),
                max(item.first, maxX),
                min(item.second, minY),
                max(item.second, maxY)
            )
        }

        return buildString {
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    append(
                        when (x to y) {
                            rope[0] -> 'H'
                            in rope -> rope.indexOf(x to y).digitToChar()
                            0 to 0 -> 's'
                            in visitedCells -> '#'
                            else -> '.'
                        }
                    )
                }
                append('\n')
            }
        }
    }
}

fun sign(value: Int) = when {
    value == 0 -> 0
    value > 0 -> 1
    else -> -1
}

fun solve(ropeLength: Int) = File("input.txt")
    .readLines()
    .fold(State(ropeLength)) { state, command ->
        // println("{{{ $command }}}")
        val (headVx, headVy) = when (command[0]) {
            'R' -> 1 to 0
            'L' -> -1 to 0
            'D' -> 0 to 1
            'U' -> 0 to -1
            else -> throw IllegalArgumentException()
        }
        repeat(command.drop(2).toInt()) {
            state.moveHead(headVx, headVy)
            // println(state)
        }
        state
    }
    .visitedCellsCount

println(solve(1))
println(solve(9))
