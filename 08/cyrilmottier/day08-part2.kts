import java.io.File

val forest = File("input.txt").readLines()
    .map { line -> line.map { it.digitToInt() } }

val forestWidth = forest[0].size
val forestHeight = forest.size

var maxScene = Int.MIN_VALUE
for (row in 0 until forestHeight) {
    for (col in 0 until forestWidth) {
        val treeHeight = forest[row][col]
        // Left
        var left = 0
        for (i in 1 until forestWidth) {
            val c = col - i
            if (c < 0) break
            left++
            if (forest[row][c] >= treeHeight) {
                break
            }
        }
        // Right
        var right = 0
        for (i in 1 until forestWidth) {
            val c = col + i
            if (c >= forestWidth) break
            right++
            if (forest[row][c] >= treeHeight) {
                break
            }
        }
        // Top
        var top = 0
        for (i in 1 until forestWidth) {
            val r = row - i
            if (r < 0) break
            top++
            if (forest[r][col] >= treeHeight) {
                break
            }
        }
        // Bottom
        var bottom = 0
        for (i in 1 until forestWidth) {
            val r = row + i
            if (r >= forestHeight) break
            bottom++
            if (forest[r][col] >= treeHeight) {
                break
            }
        }
        val scene = left * top * right * bottom
        if (scene > maxScene) {
            maxScene = scene
        }
    }
}

maxScene

