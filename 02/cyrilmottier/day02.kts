import java.io.File

File("input.txt")
    .readLines()
    .map {
        when(it) {
            "C X", "A Y", "B Z" -> 6
            "A X", "B Y", "C Z" -> 3
            "B X", "C Y", "A Z" -> 0
            else -> 0
        } + it[2].toInt() - 87
    }
    .sum()
