import java.io.File
import kotlin.math.abs

val CUBES = File("input.txt").readLines()
    .map {
        it.split(",").map { it.toInt() }
    }

fun joinCount(cubes: List<List<Int>>, dimension: Int): Int {
    var result = 0
    for (i in 0 until CUBES.size) {
        val firstCube = cubes[i]
        for (j in i + 1 until CUBES.size) {
            val secondCube = cubes[j]
            if (
                abs(firstCube[dimension] - secondCube[dimension]) == 1 &&
                firstCube[(dimension + 1) % 3] == secondCube[(dimension + 1) % 3] &&
                firstCube[(dimension + 2) % 3] == secondCube[(dimension + 2) % 3]
            ) {
                result++
            }
        }
    }
    return result
}

val xJoinCount = joinCount(CUBES, 0)
val yJoinCount = joinCount(CUBES, 1)
val zJoinCount = joinCount(CUBES, 2)

CUBES.size * 6 - xJoinCount * 2 - yJoinCount * 2 - zJoinCount * 2

