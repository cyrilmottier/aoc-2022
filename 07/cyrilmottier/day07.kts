import java.io.File

class Node(
    val name: String,
    val parent: Node? = null,
    val size: Int? = null
) {
    val children = mutableListOf<Node>()
    val computedSize: Int by lazy {
        if (children.isEmpty()) {
            size!!
        } else {
            children.sumOf { it.computedSize }
        }
    }
}

val tree = File("input.txt")
    .readLines()
    .drop(2)
    .fold(with(Node("/")) { this to this }) { (root, currentNode), line ->
        when (line[0]) {
            '$' -> when (line.slice(2..3)) {
                    "cd" -> {
                        val dirName = line.drop(5)
                        when (dirName) {
                            ".." -> root to currentNode.parent!!
                            else -> root to currentNode.children.first { it.name == dirName }
                        }
                    }
                    else -> root to currentNode
            }
            else -> {
                currentNode.children += when (line[0]) {
                    'd' -> Node(line.drop(4), currentNode)
                    else -> with(line.split(" ")) { Node(this[1], currentNode, this[0].toInt()) }
                }
                root to currentNode
            }
        }
    }
    .first

var part1 = 0
var part2 = Int.MAX_VALUE
val stack = ArrayDeque<Node>()
stack.addLast(tree)
val spaceToFind = 30_000_000 - (70_000_000 - tree.computedSize)
while (stack.isNotEmpty()) {
    val item = stack.removeLast()
    stack.addAll(item.children.filter { it.children.isNotEmpty() })
    val computedSize = item.computedSize
    if (computedSize < 100_000) {
        part1 += computedSize
    }
    if (computedSize > spaceToFind && computedSize < part2) {
        part2 = computedSize
    }
}

println(part1)
println(part2)
