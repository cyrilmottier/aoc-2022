import java.io.File

val history = File("input.txt")
    .readLines()
    .fold(mutableListOf<Int>() to 1) { (history, register), item ->
        val (cycleCount, newRegister) = when (item.slice(0..3)) {
            "noop" -> 1 to register
            "addx" -> 2 to (register + item.drop(5).toInt())
            else -> throw IllegalArgumentException()
        }
        repeat(cycleCount) { history.add(register) }
        history to newRegister
    }
    .first

val part1 = history.foldIndexed(0) { index, acc, register ->
    acc + if (index % 40 - 19 == 0) (index + 1) * register else 0
}
println(part1)

val part2 = buildString {
    history.forEachIndexed { cycle, register ->
        val col = cycle.mod(40)
        if (cycle != 0 && col == 0) append('\n')
        append(if (col in register - 1..register + 1) '#' else '.')
    }
}
println(part2)
