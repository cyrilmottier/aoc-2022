import java.io.File
import java.util.*
import kotlin.math.max

sealed class Packet() : Comparable<Packet>
data class ListPacket(val list: MutableList<Packet> = mutableListOf()) : Packet() {
    override fun compareTo(other: Packet): Int {
        return when (other) {
            is ListPacket -> {
                fun test(): Int {
                    for (i in 0 until max(list.size, other.list.size)) {
                        if (i >= list.size) return 1
                        if (i >= other.list.size) return -1
                        val cmp = list[i].compareTo(other.list[i])
                        when (cmp) {
                            -1 -> return -1
                            1 -> return 1
                        }
                    }
                    return 0
                }
                test()
            }
            is NumberPacket -> compareTo(ListPacket(mutableListOf(other)))
            else -> throw IllegalArgumentException()
        }
    }

    override fun toString() = list.joinToString(",", "[", "]")
}

data class NumberPacket(val value: Int) : Packet() {
    override fun compareTo(other: Packet): Int {
        return when (other) {
            is ListPacket -> ListPacket(mutableListOf(this)).compareTo(other)
            is NumberPacket -> {
                if (value == other.value) 0
                else if (value < other.value) 1
                else -1
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun toString() = value.toString()
}

fun parsePacket(packet: String): Packet {
    val stack = Stack<ListPacket>()
    var index = 0
    while (index < packet.length) {
        when (packet[index]) {
            '[' -> {
                val listPacket = ListPacket()
                if (stack.isNotEmpty()) {
                    stack.peek().list.add(listPacket)
                }
                stack.push(listPacket)
            }
            ']' -> if (stack.size > 1) stack.pop()
            ',' -> {}
            else -> {
                var count = 0
                while (packet[index + count] in '0'..'9') count++
                stack.peek().list.add(
                    NumberPacket(packet.slice(index..index + count - 1).toInt())
                )
            }
        }
        index++
    }
    return stack.pop()
}

val part1 = File("input.txt").readLines()
    .windowed(2, 3)
    .map {
        parsePacket(it[0]).compareTo(parsePacket(it[1]))
    }
    .withIndex()
    .filter { it.value == 1 }
    .sumOf { it.index + 1 }

println(part1)

val part2 = (File("input.txt").readLines() + listOf("[[2]]", "[[6]]"))
    .filter { it != "" }
    .map { parsePacket(it) }
    .sortedDescending()
    .map { it.toString() }
    .let {
        (it.indexOf("[[2]]") + 1) * (it.indexOf("[[6]]") + 1)
    }

println(part2)


