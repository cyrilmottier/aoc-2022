import java.io.File

4 + File("input.txt").readLines()
    .first()
    .windowed(4, 1)
    .indexOfFirst { it.groupBy { it }.size == 4 }
