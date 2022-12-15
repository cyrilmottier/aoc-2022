import java.io.File
import kotlin.math.abs
import kotlin.math.max

val SENSOR_REGEX = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

val sensors = File("input.txt").readLines()
    .map {
        SENSOR_REGEX.find(it)!!.destructured.toList().map { it.toInt() }
    }

fun impossibleColsForRow(row: Int): List<IntRange> = sensors
    .mapNotNull { (sensorX, sensorY, beaconX, beaconY) ->
        val dy = abs(sensorY - row)
        val dist = abs(sensorX - beaconX) + abs(sensorY - beaconY)
        if (dy > dist) return@mapNotNull null
        val dx = dist - dy
        (sensorX - dx)..(sensorX + dx)
    }
    .sortedBy { it.start }
    .fold(mutableListOf<IntRange>()) { acc, item ->
        acc.apply {
            if (size == 0) {
                add(item)
            } else {
                val last = acc.last()
                if (item.start > last.endInclusive) {
                    add(item)
                } else {
                    this[size - 1] = last.start..max(last.endInclusive, item.endInclusive)
                }
            }
        }
    }

val part1 = impossibleColsForRow(2_000_000)
    .sumOf { it.endInclusive - it.start }

println(part1)

val part2 = (0..4_000_000).asSequence()
    .map { impossibleColsForRow(it) }
    .withIndex()
    .first { it.value.size > 1 }
    .let {
        (it.value[0].endInclusive + 1) * 4_000_000L + it.index
    }

println(part2)

