import java.util.*

data class Valve(val name: String, val rate: Int, val names: List<String>, var opened: Boolean = false, var lastRateA: Int = -1, var lastRateB: Int = -1) {
    val list: MutableList<Pair<Valve, Int>> = mutableListOf()
    var lastVisitedRate: Int = -1
    companion object {
        fun parse(s: String): Valve {
            val scanner = Scanner(s)
            scanner.useDelimiter(", |[,; ]")
            //scanner.useDelimiter(":")
            val name = scanner.skip("Valve ").next()
            val rate = scanner.skip(" has flow rate=").nextInt()
            scanner.skip("; tunnels? leads? to valves?")
            val names = mutableListOf<String>()
            while (scanner.hasNext()) {
                names.add(scanner.next())
            }

            return Valve(name, rate, names)
        }
    }
}
fun MutableList<Valve>.fillList() {
    forEach { v ->
        v.names.forEach { name ->
            v.list.add(Pair(find { it.name == name }!!, 1))
        }
    }
}
fun MutableList<Valve>.optimize() {
    do {
        val v = find { it.rate == 0 && it.name != "AA" } ?: return
        val leads = filter { it.list.map { it.first }.contains(v) }
        leads.forEach { lead ->
            val link = lead.list.first { it.first == v }
            lead.list.remove(link)
            val listCopy = v.list.filter { it.first != lead }.toMutableList()
            for ((i, e) in listCopy.withIndex()) listCopy[i] = Pair(e.first, e.second + link.second)
            lead.list.addAll(listCopy)
        }
        remove(v)
    } while (true)
}
fun Valve.maxPressure(valves: MutableList<Valve>, time: Int, releasedRate: Int): Int {
    val additionalPressure: Int

    // time is out
    if (time <= 0) return 0
    // last...Rate - prevent cycling
    if (releasedRate == lastVisitedRate) return 0

//    println("$name $time $releasedRate")
    var foundSubPressure = 0
    if (!opened) {
        val lastClosedRate = lastVisitedRate
        lastVisitedRate = releasedRate
        list.forEach { node ->
            foundSubPressure = maxOf(foundSubPressure, node.first.maxPressure(valves, time - node.second, releasedRate))
        }
        lastVisitedRate = lastClosedRate
        if (name == "AA") return foundSubPressure

        opened = true
        additionalPressure = rate * (time - 1)
//        println("open")
        // no more nodes -> time * pressure
        if (valves.all { it.opened || it.name == "AA" }) {
            opened = false
            return additionalPressure
        }

        val lastOpenedRate = lastVisitedRate
        lastVisitedRate = releasedRate
        list.forEach { node ->
            foundSubPressure = maxOf(foundSubPressure, additionalPressure + node.first.maxPressure(valves, time - 1 - node.second, releasedRate + rate))
        }
        lastVisitedRate = lastOpenedRate
        opened = false
    } else /* opened */ {
        val lastOpenedRate = lastVisitedRate
        lastVisitedRate = releasedRate
        list.forEach { node ->
            foundSubPressure = maxOf(foundSubPressure, node.first.maxPressure(valves, time - node.second, releasedRate))
        }
        lastVisitedRate = lastOpenedRate
    }

    return foundSubPressure
}
fun MutableList<Valve>.maxPressure(nodeA: Pair<Valve, Int>, nodeB: Pair<Valve, Int>, time: Int, releasedRate: Int, counter: Int): Int {

    val a = nodeA.first // target valve
    val b = nodeB.first
    val spentTime = minOf(nodeA.second, nodeB.second) // time to target (1+)
    val timeLeft = time - spentTime
    val waitA = nodeA.second - spentTime // 0+
    val waitB = nodeB.second - spentTime

    println("  ".repeat(counter) + "${a.name} ${b.name} time $timeLeft rate $releasedRate    ${this.filter { it.opened }.joinToString { it.name }}")

    // time is out
    if (timeLeft < 2) return 0 // it takes min 2 to open and release

    // last...Rate - prevent cycling
    if (a.lastRateA == releasedRate || b.lastRateB == releasedRate) {
        println("  ".repeat(counter) + "ret lastRate ${if (a.lastRateA == releasedRate) "a" else ""} ${if (b.lastRateB == releasedRate) "b" else ""}")
        return 0
    }

    var foundSubPressure = 0

    val tempRateA = a.lastRateA
    val tempRateB = b.lastRateB
//    val tempRateB = b.lastRateA
    a.lastRateA = releasedRate
    b.lastRateB = releasedRate
//    b.lastRateA = releasedRate

//    println(" ".repeat(counter) + "${a.name} ${b.name} $timeLeft $releasedRate    ${this.filter { it.opened }.joinToString { it.name }}")
    if (a != b && !a.opened && waitA == 0 && a.name != "AA" && !b.opened && waitB == 0 && b.name != "AA") {
        a.opened = true
        b.opened = true
        val additionalPressure = (a.rate + b.rate) * (timeLeft - 1)
        if (all { it.opened || it.name == "AA" }) {
            a.opened = false
            b.opened = false
            a.lastRateA = tempRateA
            b.lastRateB = tempRateB
//            b.lastRateA = tempRateB
            return additionalPressure
        }
        foundSubPressure = maxOf(foundSubPressure, additionalPressure + maxPressure(Pair(a, 0), Pair(b, 0), timeLeft - 1, releasedRate + a.rate + b.rate, counter + 1))
        a.opened = false
        b.opened = false
    }
    if (!a.opened && waitA == 0 && a.name != "AA") {
        a.opened = true
        val additionalPressure = a.rate * (timeLeft - 1)
        if (all { it.opened || it.name == "AA" }) {
            a.opened = false
            a.lastRateA = tempRateA
            b.lastRateB = tempRateB
//            b.lastRateA = tempRateB
            return additionalPressure
        }
        for (node in b.list)
            foundSubPressure = maxOf(foundSubPressure, additionalPressure + maxPressure(Pair(a, 0), node, timeLeft - 1, releasedRate + a.rate, counter + 1))
        a.opened = false
    }
    if (!(a == b && waitA == 0) && !b.opened && waitB == 0 && b.name != "AA") {
        b.opened = true
        val additionalPressure = b.rate * (timeLeft - 1)
        if (all { it.opened || it.name == "AA" }) {
            b.opened = false
            a.lastRateA = tempRateA
            b.lastRateB = tempRateB
//            b.lastRateA = tempRateB
            return additionalPressure
        }
        for (node in a.list)
            foundSubPressure = maxOf(foundSubPressure, additionalPressure + maxPressure(node, Pair(b, 0), timeLeft - 1, releasedRate + b.rate, counter + 1))
        b.opened = false
    }

    if (waitA > 0)
        for (node in b.list)
            foundSubPressure = maxOf(foundSubPressure, maxPressure(Pair(a, waitA), node, timeLeft, releasedRate, counter + 1))
    else if (waitB > 0)
        for (node in a.list)
            foundSubPressure = maxOf(foundSubPressure, maxPressure(node, Pair(b, waitB), timeLeft, releasedRate, counter + 1))
    else // waitA & waitB = 0
        for (nodeA in a.list)
            for (nodeB in b.list)
                foundSubPressure = maxOf(foundSubPressure, maxPressure(nodeA, nodeB, timeLeft, releasedRate, counter + 1))

    a.lastRateA = tempRateA
    b.lastRateB = tempRateB
//    b.lastRateA = tempRateB

    return foundSubPressure
}
fun main() {
    fun part1(input: List<String>, time: Int): Int {
        val valves = mutableListOf<Valve>()
        input.forEach { line ->
            valves.add(Valve.parse(line))
        }
        val rootValve = valves.first {it.name == "AA" }
        valves.fillList()

        valves.optimize()

        return rootValve.maxPressure(valves, time, 0)
    }

    fun part2(input: List<String>, time: Int): Int {
        val valves = mutableListOf<Valve>()
        input.forEach { line ->
            valves.add(Valve.parse(line))
        }
        valves.fillList()

        valves.optimize()

        val rootNode = valves.first {it.name == "AA" } to 0
        return valves.maxPressure(rootNode, rootNode, time, 0, 0)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day16_test")
    val testInput = readInput("Day16_test2")
//    println(part1(testInput))
//    check(part1(testInput, 30) == 1651)
    println(part2(testInput, 26))
//    check(part2(testInput, 26) == 1707)

    val input = readInput("Day16")
//    println(part1(input, 30))
//    println(part2(input, 26))
}
