import java.io.File

data class Tree(
    val height: Int,
    var visibilityCount: Int = 0
)

val forest = File("input.txt").readLines()
    .map { line -> line.map { Tree(it.digitToInt()) } }

val forestWidth = forest[0].size
val forestHeight = forest.size

// Left
for (row in 0 until forestHeight) {
    var max = Int.MIN_VALUE
    for (depth in 0 until forestWidth) {
        if (forest[row][depth].height > max) {
            forest[row][depth].visibilityCount++
            max = forest[row][depth].height
            if (max == 9) {
                break
            }
        }
    }
}

// Right
for (row in 0 until forestHeight) {
    var max = Int.MIN_VALUE
    for (depth in 0 until forestWidth) {
        if (forest[row][forestWidth - 1 - depth].height > max) {
            forest[row][forestWidth - 1 - depth].visibilityCount++
            max = forest[row][forestWidth - 1 - depth].height
            if (max == 9) {
                break
            }
        }
    }
}


// Top
for (col in 0 until forestWidth) {
    var max = Int.MIN_VALUE
    for (depth in 0 until forestHeight) {
        if (forest[depth][col].height > max) {
            forest[depth][col].visibilityCount++
            max = forest[depth][col].height
            if (max == 9) {
                break
            }
        }
    }
}

// Bottom
for (col in 0 until forestWidth) {
    var max = Int.MIN_VALUE
    for (depth in 0 until forestHeight) {
        if (forest[forestWidth - 1 - depth][col].height > max) {
            forest[forestHeight - 1 - depth][col].visibilityCount++
            max = forest[forestWidth - 1 - depth][col].height
            if (max == 9) {
                break
            }
        }
    }
}

forest
    .flatMap { it }
    .count { it.visibilityCount != 0 }
