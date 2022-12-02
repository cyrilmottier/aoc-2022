import java.io.File

File("input.txt")
    .readLines()
    .map {
        val opponentMove = it.first()
        when (it.last()) {
            'X' -> "BCA".indexOf(opponentMove) + 1
            'Y' -> "ABC".indexOf(opponentMove) + 4
            'Z' -> "CAB".indexOf(opponentMove) + 7
            else -> 0
        }
    }
    .sum()
