import java.io.File
import kotlin.math.max
import kotlin.time.measureTime

val VALVE_REGEX = "Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.*)".toRegex()

data class Valve(
    val flowRate: Int,
    val neighbors: List<String>
)

val worldData = File("input.txt").readLines()
    .map {
        val (valve, flowRate, others) = VALVE_REGEX.find(it)!!.destructured
        Triple(
            valve,
            flowRate.toInt(),
            others.split(", ")
        )
    }
    .fold(mutableMapOf<String, Valve>()) { acc, (valve, flowRate, neighbors) ->
        acc[valve] = Valve(flowRate, neighbors)
        acc
    }

val world = mutableMapOf<String, MutableMap<String, Int>>()
val nonZeroPressure = mutableSetOf<String>()

worldData.forEach { valve, _ ->
    world[valve] = mutableMapOf<String, Int>()
    if (worldData[valve]!!.flowRate > 0) {
        nonZeroPressure.add(valve)
    }
    val seen = mutableSetOf(valve)
    val queue = ArrayDeque<Pair<String, Int>>(listOf(valve to 0))
    while (queue.isNotEmpty()) {
        val item = queue.removeFirst()
        worldData[item.first]!!.neighbors.forEach { neighbor ->
            if (neighbor in seen) return@forEach
            seen.add(neighbor)
            world[valve]!!.put(neighbor, item.second + 1)
            queue.addLast(neighbor to item.second + 1)
        }
    }
}

val cache = mutableMapOf<Triple<Int, String, Int>, Int>()

fun step(time: Int, valveId: String, opened: Int): Int {
    val cacheKey = Triple(time, valveId, opened)
    cache[cacheKey]?.also { return it }
    val valve = world[valveId]!!
    var max = 0
    valve.forEach { v, _ ->
        if (worldData[v]!!.flowRate != 0) {
            val bit = 1.shl(nonZeroPressure.indexOf(v))
            if (opened.and(bit) == 0) {
                val t = time - valve[v]!! - 1
                if (t > 0) {
                    max = max(max, step(t, v, opened.or(bit)) + worldData[v]!!.flowRate * t)
                }
            }
        }
    }
    cache[cacheKey] = max
    return max
}

@OptIn(kotlin.time.ExperimentalTime::class)
val duration = measureTime {
    println(step(30, "AA", 0))
}
println("Step 1 done in $duration")


@OptIn(kotlin.time.ExperimentalTime::class)
val duration2 = measureTime {
    var max = 0
    val b = 1.shl(nonZeroPressure.size) - 1
    for (i in 0..b) {
        max = max(max, step(26, "AA", i) + step(26, "AA", b.xor(i)))
    }
    println(max)
}
println("Step 2 done in $duration2")
