import java.io.File

val forest = File("input.txt").readLines()
    .map { line -> line.map { it.digitToInt() } }

val forestWidth = forest[0].size
val forestHeight = forest.size

fun t(row: Int, col: Int, rowDir: Int, colDir: Int): Int {
    var result = 0
    for (i in 1 until forestWidth) {
        val r = row - rowDir * i
        val c = col - colDir * i
        if (c < 0 || c >= forestWidth || r < 0 || r >= forestHeight) break
        result++
        if (forest[r][c] >= forest[row][col]) {
            break
        }
    }
    return result
}

var maxScene = Int.MIN_VALUE
for (row in 0 until forestHeight) {
    for (col in 0 until forestWidth) {
        val left = t(row, col, 0, -1)
        val right = t(row, col, 0, 1)
        val top = t(row, col, -1, 0)
        val bottom = t(row, col, 1, 0)

        val scene = left * top * right * bottom
        if (scene > maxScene) {
            maxScene = scene
        }
    }
}

maxScene

