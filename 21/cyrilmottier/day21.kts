import java.io.File
import kotlin.time.measureTime

sealed class Expression
data class Constant(val value: Long) : Expression()
data class Operation(val operation: Char, val left: String, val right: String) : Expression()

val expressions = File("input.txt").readLines()
    .fold(mutableMapOf<String, Expression>()) { acc, line ->
        val (variableName, expression) = line.split(": ")
        val constant = expression.toLongOrNull()
        acc[variableName] = if (constant != null) {
            Constant(constant)
        } else {
            val left = expression.slice(0..3)
            val right = expression.slice(7..10)
            val operation = expression[5]
            Operation(operation, left, right)
        }
        acc
    }

fun compute(varName: String): Long {
    val result = expressions[varName]
    return when (result) {
        is Constant -> result.value
        is Operation -> {
            val operation: Long.(Long) -> Long = when (result.operation) {
                '+' -> Long::plus
                '-' -> Long::minus
                '*' -> Long::times
                '/' -> Long::div
                else -> throw UnsupportedOperationException()
            }
            compute(result.left).operation(compute(result.right))
        }
        else -> throw IllegalStateException()
    }
}

@OptIn(kotlin.time.ExperimentalTime::class)
val duration = measureTime {
    println(compute("root"))
}
println("Took $duration")

