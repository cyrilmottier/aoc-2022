import java.io.File
import kotlin.math.max

val jetPattern = File("input.txt").readText().replace("\n", "")

val ROCKS = listOf(
    listOf("####"),
    listOf(
        ".#.",
        "###",
        ".#.",
    ),
    listOf(
        "..#",
        "..#",
        "###",
    ),
    listOf(
        "#",
        "#",
        "#",
        "#",
    ),
    listOf(
        "##",
        "##",
    ),
)
val CHAMBER = mutableListOf<MutableList<Char>>()
val ROCK_COUNT = 2022
val DEBUG = false

fun growChamber(
    chamber: MutableList<MutableList<Char>>,
    rockHeight: Int
) {
    val emptyRowCount = chamber.reversed().indexOfFirst { '#' in it }
    val additionalRowCount = 3 + rockHeight - if (emptyRowCount == -1) 0 else emptyRowCount

    chamber.apply {
        repeat(additionalRowCount) {
            add(MutableList(7) { '.' })
        }
    }
}

fun dumpChamber(
    chamber: MutableList<MutableList<Char>>,
    rock: List<String>? = null,
    rockX: Int = 0,
    rockY: Int = 0,
): String {
    return buildString {
        for (y in chamber.size - 1 downTo 0) {
            for (x in 0 until chamber[0].size) {
                append(
                    if (rock != null &&
                        x >= rockX &&
                        x < rockX + rock[0].length &&
                        y <= rockY &&
                        y > rockY - rock.size
                    ) {
                        if (rock[-1 * (y - rockY)][x - rockX] == '#') '@' else chamber[y][x]
                    } else {
                        chamber[y][x]
                    }
                )
            }
            append('\n')
        }
    }
}

fun canMoveRock(
    chamber: MutableList<MutableList<Char>>,
    rock: List<String>,
    rockX: Int,
    rockY: Int,
    rockDx: Int,
    rockDy: Int
): Boolean {
    val rockWidth = rock[0].length
    val rockHeight = rock.size

    if (rockDx != 0) {
        if (rockX + rockDx < 0 ||
            rockX + rockDx + rockWidth > chamber[0].size
        ) {
            return false
        }
    }
    if (rockDy != 0) {
        if (rockY - rockHeight < 0) {
            return false
        }
    }

    for (x in 0 until rockWidth) {
        for (y in (rockHeight - 1) downTo 0) {
            if (rock[y][x] == '#') {
                if (chamber[rockY + rockDy - y][rockX + rockDx + x] == '#') {
                    return false
                }
            }
        }
    }
    return true
}

fun moveRock(
    world: MutableList<MutableList<Char>>,
    rock: List<String>,
    rockX: Int,
    rockY: Int,
) {
    val rockWidth = rock[0].length
    val rockHeight = rock.size
    for (x in 0 until rockWidth) {
        for (y in 0 until rockHeight) {
            world[rockY - y][rockX + x] = rock[y][x]
        }
    }
}

var jetIndex = 0
var rockCount = 0
while (rockCount < ROCK_COUNT) {

    val rockIndex = rockCount % ROCKS.size
    val rock = ROCKS[rockIndex]
    val rockWidth = rock[0].length
    val rockHeight = rock.size

    // Grow the chamber
    growChamber(CHAMBER, rockHeight)

    val indexOfLastNonEmptyLine = CHAMBER.indexOfFirst { it.all { it == '.' } } // Could read in reverse order to be faster
    var rockX = 2
    var rockY = max(0, indexOfLastNonEmptyLine) + 3 + rockHeight - 1

    if (DEBUG) {
        println("Rock #${rockCount + 1} (index=$rockIndex) begins falling")
        println(dumpChamber(CHAMBER, rock, rockX, rockY))
    }

    while (true) {
        val rockDx = when (jetPattern[jetIndex]) {
            '>' -> 1
            '<' -> -1
            else -> 0
        }
        jetIndex = (jetIndex + 1) % jetPattern.length
        if (DEBUG) {
            print("Jet of gas pushes rock to $rockDx")
        }
        if (canMoveRock(CHAMBER, rock, rockX, rockY, rockDx, 0)) {
            rockX = rockX + rockDx
            if (DEBUG) {
                println(":")
            }
        } else {
            if (DEBUG) {
                println(", but nothing happens:")
            }
        }
        if (DEBUG) {
            println(dumpChamber(CHAMBER, rock, rockX, rockY))
        }

        if (DEBUG) {
            print("Rock falls 1 unit")
        }
        if (canMoveRock(CHAMBER, rock, rockX, rockY, 0, -1)) {
            rockY = rockY - 1
            if (DEBUG) {
                println(":")
                println(dumpChamber(CHAMBER, rock, rockX, rockY))
            }
        } else {
            moveRock(CHAMBER, rock, rockX, rockY)
            if (DEBUG) {
                println(", causing it to come to rest:")
                println(dumpChamber(CHAMBER))
            }
            break
        }
    }

    rockCount++
}

CHAMBER.indexOfFirst { it.all { it == '.' } }
