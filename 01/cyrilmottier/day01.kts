import java.io.File

val maxCalories = File("input.txt")
    .readLines()
    .fold(mutableListOf<Int>()) { acc, item ->
        val number = item.toIntOrNull()
        if (number != null) {
            if (acc.size == 0) {
                acc.add(number)
            } else {
                acc[acc.size - 1] += number
            }
        } else {
            acc.add(0)
        }
        acc
    }
    .max()

println(maxCalories)
