import java.io.File

data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val divisibleBy: Int,
    val trueThrow: Int,
    val falseThrow: Int,
    var passes: Int = 0
)

fun solve(times: Int, divide: Int): Long {
    var modulo = 1
    val monkeys = File("input.txt").readText()
        .split("\n\n")
        .map { monkey ->
            val lines = monkey.split("\n")

            val items = lines[1].split(": ")[1].split(", ").map { it.toLong() }.toMutableList()
            val operation = lines[2].split(" = ")[1]
                .let {
                    val (_, operator, operand) = it.split(" ")
                    val f = mapOf<String, Long.(Long) -> Long>("*" to Long::times, "+" to Long::plus)[operator]!!
                    { old: Long -> old.f(operand.toLongOrNull() ?: old) }
                }

            val divisibleBy = lines[3].split("y ")[1].toInt()
            val trueThrow = lines[4].split("y ")[1].toInt()
            val falseThrow = lines[5].split("y ")[1].toInt()

            modulo *= divisibleBy

            Monkey(
                items,
                operation,
                divisibleBy,
                trueThrow,
                falseThrow
            )
        }

    repeat(times) {
        monkeys.forEach { monkey ->
            monkey.passes += monkey.items.size
            monkey.items.forEach { item ->
                val level = (monkey.operation(item) / divide) % modulo
                val newIndex = if (level % monkey.divisibleBy == 0L) monkey.trueThrow else monkey.falseThrow
                monkeys[newIndex].items.add(level)
            }
            monkey.items.clear()
        }
    }

    return monkeys
        .map { it.passes }
        .sortedDescending()
        .take(2)
        .fold(1L) { acc, item -> acc * item.toLong() }
}

println(solve(20, 3))
println(solve(10000, 1))
