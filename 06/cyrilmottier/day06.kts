import java.io.File

fun startOfMessageIndex(length: Int) = length + File("input.txt").readLines()
    .first()
    .windowed(length, 1)
    .indexOfFirst { it.groupBy { it }.size == length }

println(startOfMessageIndex(4))
println(startOfMessageIndex(14))
