import java.io.File

val SECTION_REG_EX = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()

File("input.txt").readLines()
    .map { SECTION_REG_EX.find(it)!!.destructured.toList().map { it.toInt() } }
    .count {
        (it[0] >= it[2] && it[0] <= it[3]) ||
                (it[1] >= it[2] && it[1] <= it[3]) ||
                (it[2] >= it[0] && it[2] <= it[1]) ||
                (it[3] >= it[0] && it[3] <= it[1])
    }
