import java.util.*

val solutions = mutableMapOf<Int, Int>()

data class Link(val valve: Valve, val dist: Int)
data class Valve(val name: String, val rate: Int, var opened: Boolean = false, var lastRateA: Int = -1, var lastRateB: Int = -1) {
    lateinit var names: List<String>
    val links: MutableList<Link> = mutableListOf()
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

            val valve = Valve(name, rate)
            valve.names = names
            return valve
        }
    }
}
fun MutableList<Valve>.fillList() {
    forEach { v ->
        v.names.forEach { name ->
            v.links.add(Link(find { it.name == name }!!, 1))
        }
    }
}
fun MutableList<Valve>.optimize() {
    do {
        val v = find { it.rate == 0 && it.name != "AA" } ?: return
        val leads = filter { it.links.map { it.valve }.contains(v) }
        leads.forEach { lead ->
            val link = lead.links.first { it.valve == v }
            lead.links.remove(link)
            val listCopy = v.links.filter { it.valve != lead }.toMutableList()
            for ((i, e) in listCopy.withIndex()) listCopy[i] = Link(e.valve, e.dist + link.dist)
            lead.links.addAll(listCopy)
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
        links.forEach { link ->
            foundSubPressure = maxOf(foundSubPressure, link.valve.maxPressure(valves, time - link.dist, releasedRate))
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
        links.forEach { link ->
            foundSubPressure = maxOf(foundSubPressure, additionalPressure + link.valve.maxPressure(valves, time - 1 - link.dist, releasedRate + rate))
        }
        lastVisitedRate = lastOpenedRate
        opened = false
    } else /* opened */ {
        val lastOpenedRate = lastVisitedRate
        lastVisitedRate = releasedRate
        links.forEach { link ->
            foundSubPressure = maxOf(foundSubPressure, link.valve.maxPressure(valves, time - link.dist, releasedRate))
        }
        lastVisitedRate = lastOpenedRate
    }

    return foundSubPressure
}
fun MutableList<Valve>.maxPressure(linkA: Link, linkB: Link, time: Int, releasedRateA: Int, releasedRateB: Int, depth: Int, result: Int): Int {

    val a = linkA.valve // target valve
    val b = linkB.valve
    val spentTime = minOf(linkA.dist, linkB.dist) // time to target (1+)
    val timeLeft = time - spentTime
    val waitA = linkA.dist - spentTime // 0+
    val waitB = linkB.dist - spentTime

//    println("  ".repeat(counter) + "${a.name} ${b.name} time $timeLeft rate $releasedRate    ${this.filter { it.opened }.joinToString { it.name }}")

    // time is out
    if (timeLeft < 2) return 0 // it takes min 2 to open and release
    if (timeLeft < 14) {
//        if (solutions[result] == null) solutions[result] = 1
//        else solutions[result] = solutions[result]!! + 1
        if (result < 2000) return 0
    }

    // last...Rate - prevent cycling
//    if (a.lastRateA == releasedRateA || b.lastRateB == releasedRateB) {
//        // TODO: remove
////        println("  ".repeat(counter) + "ret lastRate ${if (a.lastRateA == releasedRate) "a" else ""} ${if (b.lastRateB == releasedRate) "b" else ""}")
//        return 0
//    }

    var foundSubPressure = 0

    val tempRateA = a.lastRateA
    val tempRateB = b.lastRateB
    if (waitA == 0)
        a.lastRateA = releasedRateA
    if (waitB == 0)
        b.lastRateB = releasedRateB

    if (waitA > 0) {
        if (!b.opened && b.name != "AA") { // closed & !AA so we open
            val openedPressure = b.rate * (timeLeft - 1)
            if (all { it.opened || it.name == "AA" || it == b }) {
                a.lastRateA = tempRateA
                b.lastRateB = tempRateB
                return openedPressure
            }
            b.opened = true
            b.lastRateB = releasedRateB + b.rate
            for (linkB in b.links)
                foundSubPressure = maxOf(foundSubPressure,
                    openedPressure + maxPressure(Link(a, waitA - 1), linkB, timeLeft - 1, releasedRateA, releasedRateB + b.rate, depth + 1, openedPressure + result))
            b.opened = false
            b.lastRateB = releasedRateB
        }
        val links = b.links.filter { it.valve.lastRateB < releasedRateB }
        if (links.isEmpty() && b.opened)
            foundSubPressure = maxOf(foundSubPressure, maxPressure(Link(a, waitA), Link(b, 1000), timeLeft, releasedRateA, releasedRateB, depth + 1, result))
        else
            for (linkB in links)
                foundSubPressure = maxOf(foundSubPressure, maxPressure(Link(a, waitA), linkB, timeLeft, releasedRateA, releasedRateB, depth + 1, result))

    } else if (waitB > 0) {
        if (!a.opened && a.name != "AA") { // closed && !AA so we open
            val openedPressure = a.rate * (timeLeft - 1)
            if (all { it.opened || it.name == "AA" || it == a }) {
                a.lastRateA = tempRateA
                b.lastRateB = tempRateB
                return openedPressure
            }
            a.opened = true
            a.lastRateA = releasedRateA + a.rate
            for (linkA in a.links)
                foundSubPressure = maxOf(foundSubPressure,
                    openedPressure + maxPressure(linkA, Link(b, waitB - 1), timeLeft - 1, releasedRateA + a.rate, releasedRateB, depth + 1, openedPressure + result))
            a.opened = false
            a.lastRateA = releasedRateA
        }
        val links = a.links.filter { it.valve.lastRateA < releasedRateA }
        if (links.isEmpty() && a.opened)
            foundSubPressure = maxOf(foundSubPressure, maxPressure(Link(a, 1000), Link(b, waitB), timeLeft, releasedRateA, releasedRateB, depth + 1, result))
        else
            for (linkA in links)
                foundSubPressure = maxOf(foundSubPressure, maxPressure(linkA, Link(b, waitB), timeLeft, releasedRateA, releasedRateB, depth + 1, result))
    } else // waitA & waitB = 0
        if (a == b) {
            if (!a.opened && a.name != "AA") { //
                val openedPressure = a.rate * (timeLeft - 1)
                if (all { it.opened || it.name == "AA" || it == a }) {
                    a.lastRateA = tempRateA
                    b.lastRateB = tempRateB
                    return openedPressure
                }
                a.opened = true // a open, b pass
                a.lastRateA = releasedRateA + a.rate
                val links = b.links.filter { it.valve.lastRateB < releasedRateB }
                if (links.isEmpty())
                    foundSubPressure = maxOf(foundSubPressure,
                        openedPressure + maxPressure(Link(a, 0), Link(b, 1000), timeLeft - 1, releasedRateA + a.rate, releasedRateB, depth + 1, openedPressure + result))
                else
                    for (linkB in links)
                        foundSubPressure = maxOf(foundSubPressure,
                            openedPressure + maxPressure(Link(a, 0), Link(linkB.valve, linkB.dist - 1), timeLeft - 1, releasedRateA + a.rate, releasedRateB, depth + 1, openedPressure + result))
                a.opened = false
                a.lastRateA = releasedRateA
            }
            // both pass thru
            val linksA = a.links.filter { it.valve.lastRateA < releasedRateA }
            val linksB = b.links.filter { it.valve.lastRateB < releasedRateB }
            if (linksA.isEmpty())
                if (linksB.isEmpty())
//                    return 0
                else
                    for (linkB in linksB)
                        foundSubPressure = maxOf(foundSubPressure,
                            maxPressure(Link(a, 1000), linkB, timeLeft, releasedRateA, releasedRateB, depth + 1, result))
            else
                for (linkA in linksA)
                    if (linksB.isEmpty())
                        foundSubPressure = maxOf(foundSubPressure,
                            maxPressure(linkA, Link(b, 1000), timeLeft, releasedRateA, releasedRateB, depth + 1, result))
                    else
                        for (linkB in linksB)
                            foundSubPressure = maxOf(foundSubPressure,
                                maxPressure(linkA, linkB, timeLeft, releasedRateA, releasedRateB, depth + 1, result))

        } else { // a <> b
            if (!a.opened && a.name != "AA") {
                val openedPressureA = a.rate * (timeLeft - 1)
                if (all { it.opened || it.name == "AA" || it == a }) {
                    a.lastRateA = tempRateA
                    b.lastRateB = tempRateB
                    return openedPressureA
                }
                a.opened = true
                a.lastRateA = releasedRateA + a.rate

                if (!b.opened && b.name != "AA") {
                    val openedPressureB = b.rate * (timeLeft - 1)
                    if (all { it.opened || it.name == "AA" || it == b }) {
                        a.opened = false
                        a.lastRateA = tempRateA
                        b.lastRateB = tempRateB
                        return openedPressureA + openedPressureB
                    }
                    b.opened = true
                    b.lastRateB = releasedRateB + b.rate

                    foundSubPressure = maxOf(foundSubPressure,
                        openedPressureA + openedPressureB + maxPressure(Link(a, 0), Link(b, 0), timeLeft - 1, releasedRateA + a.rate, releasedRateB + b.rate, depth + 1, openedPressureA + openedPressureB + result))
                    b.opened = false
                    b.lastRateB = releasedRateB
                }

                val links = b.links.filter { it.valve.lastRateB < releasedRateB }
                if (links.isEmpty())
                    foundSubPressure = maxOf(foundSubPressure,
                        openedPressureA + maxPressure(Link(a, 0), Link(b, 1000), timeLeft - 1, releasedRateA + a.rate, releasedRateB, depth + 1, openedPressureA + result))
                else
                    for (linkB in links)
                        foundSubPressure = maxOf(foundSubPressure,
                            openedPressureA + maxPressure(Link(a, 0), Link(linkB.valve, linkB.dist - 1), timeLeft - 1, releasedRateA + a.rate, releasedRateB, depth + 1, openedPressureA + result))
                a.opened = false
                a.lastRateA = releasedRateA
            }
            val linksA = a.links.filter { it.valve.lastRateA < releasedRateA }
            val linksB = b.links.filter { it.valve.lastRateB < releasedRateB }
            if (linksA.isEmpty())
                if (linksB.isEmpty())
//                    return 0
                else
                    for (link in linksB)
                        foundSubPressure = maxOf(foundSubPressure,
                            maxPressure(Link(a, 1000), link, timeLeft, releasedRateA, releasedRateB, depth + 1, result))
            else
                for (linkA in linksA)
                    if (linksB.isEmpty())
                        foundSubPressure = maxOf(foundSubPressure,
                            maxPressure(linkA, Link(b, 1000), timeLeft, releasedRateA, releasedRateB, depth + 1, result))
                    else
                        for (linkB in linksB)
                            foundSubPressure = maxOf(foundSubPressure,
                                maxPressure(linkA, linkB, timeLeft, releasedRateA, releasedRateB, depth + 1, result))
        }

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

        val rootNode = Link(valves.first {it.name == "AA" }, 0)
        return valves.maxPressure(rootNode, rootNode, time - 0, 0, 0, 0, 0)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
//    val testInput = readInput("Day16_test2")
//    val testInput = readInput("Day16_test3")
//    println(part1(testInput))
//    check(part1(testInput, 30) == 1651)
//    println(part2(testInput, 26))
//    check(part2(testInput, 26) == 1707) //1652

    val input = readInput("Day16")
//    println(part1(input, 30))
    println(part2(input, 26)) // 2772
//    println(solutions.map { (key, value) -> Pair(key, value) }.sortedBy { it.first }.joinToString("\n"))
}
