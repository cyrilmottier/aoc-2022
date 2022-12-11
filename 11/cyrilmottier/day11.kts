import java.io.File

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    return this
        .flatMapIndexed { index, x ->
            when {
                index == 0 || index == this.lastIndex -> listOf(index)
                predicate(x) -> listOf(index - 1, index + 1)
                else -> emptyList()
            }
        }
        .windowed(size = 2, step = 2) { (from, to) -> this.slice(from..to) }
}

data class Monkey(
    val items: ArrayDeque<Int>,
    val operation: (Int) -> Int,
    val divisibleBy: Int,
    val trueThrow: Int,
    val falseThrow: Int,
    var inspectCount: Int = 0
)

val monkeys = File("input.txt").readLines()
    .split { it == "" }
    .map {
        val items = it[1]
            .removePrefix("  Starting items: ")
            .split(", ")
            .map { it.toInt() }
            .let { ArrayDeque(it) }

        val operation = it[2]
            .removePrefix("  Operation: new = ")
            .let {
                when {
                    "*" in it -> with(it.split(" * ")) {
                        { old: Int -> (this[0].toIntOrNull() ?: old) * (this[1].toIntOrNull() ?: old) }
                    }
                    "+" in it -> with(it.split(" + ")) {
                        { old: Int -> (this[0].toIntOrNull() ?: old) + (this[1].toIntOrNull() ?: old) }
                    }
                    else -> throw UnsupportedOperationException()
                }
            }

        val divisibleBy = it[3]
            .removePrefix("  Test: divisible by ")
            .let { it.toInt() }

        val trueThrow = it[4]
            .removePrefix("    If true: throw to monkey ")
            .let { it.toInt() }

        val falseThrow = it[5]
            .removePrefix("    If false: throw to monkey ")
            .let { it.toInt() }

        Monkey(
            items,
            operation,
            divisibleBy,
            trueThrow,
            falseThrow
        )
    }

repeat(20) {
    monkeys.forEach { monkey ->
        val removeCount = monkey.items.size
        monkey.inspectCount += removeCount
        monkey.items.forEach { item ->
            val level = monkey.operation(item) / 3
            if (level.mod(monkey.divisibleBy) == 0) {
                monkeys[monkey.trueThrow].items.addLast(level)
            } else {
                monkeys[monkey.falseThrow].items.addLast(level)
            }
        }
        repeat(removeCount) { monkey.items.removeFirst() }
    }
}

monkeys
    .map { it.inspectCount }
    .sortedDescending()
    .take(2)
    .fold(1) { acc, item -> acc * item }
