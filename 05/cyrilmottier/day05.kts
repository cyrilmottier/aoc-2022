import java.io.File

val COMMAND_REGEX = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

val lines = File("input.txt").readLines()
val splitIndex = lines.indexOf("")
val ship = lines.subList(0, splitIndex - 1)
val columnCount = lines[splitIndex - 1].last().digitToInt()
val commands = lines.subList(splitIndex + 1, lines.size)

val crates = ship.fold(List(columnCount) { ArrayDeque<Char>() }) { acc, item ->
    for (column in 0..columnCount) {
        val crate = item.toList().getOrNull(column * 4 + 1)
        if (crate != null && crate != ' ') {
            acc[column].addFirst(crate)
        }
    }
    acc
}

commands.forEach {
    val (count, startColumn, endColumn) = COMMAND_REGEX.find(it)!!.destructured.toList().map { it.toInt() }
    repeat(count) {
        crates[endColumn - 1].addLast(crates[startColumn - 1].removeLast())
    }
}

crates.fold("") { acc, item ->
    acc + item.last()
}
