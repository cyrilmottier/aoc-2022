import java.io.File

File("input.txt")
    .readLines()
    .map { ruckSack ->
        ruckSack.chunked(ruckSack.length / 2)
            .map { it.toSet() }
            .reduce { acc, item -> acc.intersect(item) }
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
