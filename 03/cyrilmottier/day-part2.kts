import java.io.File

File("input.txt")
    .readLines()
    .chunked(3)
    .map { ruckSacks ->
        ruckSacks
            .map { it.toSet() }
            .reduce { acc, it -> acc.intersect(it) }
            .first()
    }
    .map {
        when (it) {
            in 'a'..'z' -> it.code - 97 + 1
            in 'A'..'Z' -> it.code - 65 + 27
            else -> 0
        }
    }
    .sum()
