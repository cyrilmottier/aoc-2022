import java.io.File
import kotlin.math.pow

val SNAFU_TO_DIGITS = mapOf('2' to 2L, '1' to 1L, '0' to 0L, '-' to -1L, '=' to -2L)
val DIGITS_TO_SNAFU = mapOf(2L to '2', 1L to '1', 0L to '0', -1L to '-', -2L to '=')

fun String.toDecimal() = foldIndexed(0L) { index, acc, c ->
    acc + 5.0.pow(length - index - 1).toLong() * SNAFU_TO_DIGITS[c]!!
}

fun Long.toSnafu(): String {
    var number = this
    var result = ""
    var retain = 0L

    fun digit(remainder: Long) = if (remainder >= 3) {
        retain = 1L
        DIGITS_TO_SNAFU[remainder - 5]!!
    } else {
        retain = 0L
        remainder.toInt().digitToChar()
    }

    while (number >= 5) {
        val remainder = number % 5L + retain
        result += digit(remainder)
        number = number / 5
    }

    result += digit(number + retain)
    if (retain == 1L) {
        result += retain.toInt().digitToChar()
    }

    return result.reversed()
}

File("input.txt")
    .readLines()
    .sumOf { it.toDecimal() }
    .let { it.toSnafu() }
