import java.util.*
import kotlin.math.abs

data class Sensor(val x: Int, val y: Int, val radius: Int) {
    companion object {
        fun parse(s: String): Sensor {
            val scanner = Scanner(s)
            scanner.useDelimiter("[,:]")
            //scanner.useDelimiter(":")
            val x = scanner.skip("Sensor at x=").nextInt()
            val y = scanner.skip(", y=").nextInt()
            val bx = scanner.skip(": closest beacon is at x=").nextInt()
            val by = scanner.skip(", y=").nextInt()
            val radius = abs(x - bx) + abs(y - by)
            return Sensor(x, y, radius)
        }
    }
}
data class Beacon(val x: Int, val y: Int) {
    companion object {
        fun parse(s: String): Beacon {
            val scanner = Scanner(s)
            scanner.useDelimiter("[,:]")
            //scanner.useDelimiter(":")
            scanner.skip("Sensor at x=").nextInt()
            scanner.skip(", y=").nextInt()
            val bx = scanner.skip(": closest beacon is at x=").nextInt()
            val by = scanner.skip(", y=").nextInt()
            return Beacon(bx, by)
        }
    }
}
class Intervals() {
    val list = mutableListOf<IntRange>()
    fun add(a: Int, b: Int) = list.add(a..b)
    fun addLimited(a: Int, b: Int, limit: Int) = list.add(a.coerceAtLeast(0)..b.coerceAtMost(limit))
    fun overlap() {
        outer@do {
            var found = false
            for (i in 0..list.lastIndex)
                for (j in i + 1..list.lastIndex) {
                    val (a, b) = listOf(list[i], list[j])
                    if (maxOf(a.first, b.first) <= minOf(a.last, b.last)) {
                        list.add(minOf(a.first, b.first)..maxOf(a.last, b.last))
                        list.remove(a)
                        list.remove(b)
                        found = true
                        continue@outer
                    }
                }
        } while (found)
    }
    fun sum(beacons: List<Beacon>): Int {
        return list.sumOf { interval -> interval.last - interval.first + 1 - beacons.count { it.x in interval } }
    }
}
fun main() {
    fun part1(input: List<String>, y: Int): Int {
        val sensors = input.map { Sensor.parse(it) }
        val beacons = input.map { Beacon.parse(it) }.toSet()
        val intervals = Intervals()

        sensors.forEach { sensor ->
            val dist = abs(sensor.y - y)
            if (sensor.radius >= dist) {
                val width = sensor.radius - dist
                val start = sensor.x - width
                val end = sensor.x + width
                intervals.add(start, end)
            }
        }
        intervals.overlap()
        return intervals.sum(beacons.filter { it.y == y })
    }

    fun part2(input: List<String>, limit: Int): Long {
        val sensors = input.map { Sensor.parse(it) }

        for (y in 0..limit) {
            val intervals = Intervals()
            sensors.forEach { sensor ->
                val dist = abs(sensor.y - y)
                if (sensor.radius >= dist) {
                    val width = sensor.radius - dist
                    val start = sensor.x - width
                    val end = sensor.x + width
                    intervals.addLimited(start, end, limit)
                }
            }
            intervals.overlap()
            intervals.list.sortBy { it.first }
            if (intervals.list.size == 1)
                if (intervals.list[0] == 0..limit) continue
                else {
                        if (intervals.list[0].first > 0) return 0 * 4_000_000L + y
                        if (intervals.list[0].last < limit) return limit * 4_000_000L + y
                        else error("strange list")
                    }
            else return (intervals.list[0].last + 1) * 4_000_000L + y
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
//    println(part1(testInput, 10))
    check(part1(testInput, 10) == 26)
    println(part2(testInput, 20))
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
//    println(part1(input, 2000000))
    println(part2(input, 4_000_000))
}
