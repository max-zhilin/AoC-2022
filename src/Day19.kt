import java.util.*
enum class Resource {
    ORE, CLAY, OBSIDIAN, GEODE
}
data class Robot(val type: Resource, val madeOf: Map<Resource, Int>) {

    companion object {
        fun parse(s: String): Robot {
            // " Each clay robot costs 3 ore"
            // " Each obsidian robot costs 2 ore and 14 clay"
            val regex = " Each (\\w+) robot costs (\\d+) (\\w+)(?: and (\\d+) (\\w+))?".toRegex()

            return regex.matchEntire(s)
                ?.destructured
                ?.let { (type, costs1, madeOf1, costs2, madeOf2) ->
                    if (madeOf2.isBlank()) {
                        Robot(Resource.valueOf(type.uppercase()), mapOf(Resource.valueOf(madeOf1.uppercase()) to costs1.toInt()))
                    } else {
                        Robot(Resource.valueOf(type.uppercase()), mapOf(Resource.valueOf(madeOf1.uppercase()) to costs1.toInt(),
                                                                        Resource.valueOf(madeOf2.uppercase()) to costs2.toInt()))
                    }
                }
                ?: error("Bad input '$s'")        }
    }
}
data class Blueprint(val ore4Ore: Int, val ore4Clay: Int, val ore4Obsidian: Int, val clay4Obsidian: Int, val ore4Geode: Int, val obsidian4Geode: Int) {

    companion object {
        fun parse(s: String): Blueprint {
            // Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            val regex = """Blueprint \d+: Each ore robot costs (\d+) ore\. Each clay robot costs (\d+) ore\. Each obsidian robot costs (\d+) ore and (\d+) clay\. Each geode robot costs (\d+) ore and (\d+) obsidian\."""
                .toRegex()

            return regex.matchEntire(s)//?.groupValues
                //?.map { it.toInt() }
                ?.destructured
                ?.let { (ore4Ore, ore4Clay, ore4Obsidian, clay4Obsidian, ore4Geode, obsidian4Geode) ->
                        Blueprint(ore4Ore.toInt(), ore4Clay.toInt(), ore4Obsidian.toInt(), clay4Obsidian.toInt(), ore4Geode.toInt(), obsidian4Geode.toInt())
                }
                ?: error("Bad input '$s'")        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        fun parse(s: String): List<Robot> {
            val scanner = Scanner(s)
            scanner.useDelimiter("\\.")
            scanner.skip("Blueprint \\d+:")
            val list = mutableListOf<Robot>()
            while (scanner.hasNext()) {
                val line = scanner.next()
                list.add(Robot.parse(line))
            }
            return list
        }
        fun calc(bp: Blueprint, maxOres: Int, maxClays: Int, time: Int): Int {
            var (ore, clay, obsidian, geode) = listOf(0, 0, 0, 0)
            var oreRobots = 1
            var (clayRobots, obsidianRobots, geodeRobots) = listOf(0, 0, 0)
            repeat(time) {

                with(bp) {
                    val oreReserve = if (obsidian4Geode - obsidian <= obsidianRobots) ore4Geode
                    else if (clay4Obsidian - clay <= clayRobots) ore4Obsidian
                    //else if (ore4Clay - ore <= oreRobots) ore4Clay
                    else 0

                    if (obsidian >= obsidian4Geode && ore >= ore4Geode) {
                        geodeRobots++
                        geode-- // compensate this turn production
                        obsidian -= obsidian4Geode
                        ore -= ore4Geode
                    } else if (clay >= clay4Obsidian && ore >= ore4Obsidian) {
                        obsidianRobots++
                        obsidian--
                        clay -= clay4Obsidian
                        ore -= ore4Obsidian
                    } else if (clayRobots < maxClays && ore - oreReserve >= ore4Clay) {
                        clayRobots++
                        clay--
                        ore -= ore4Clay
                    } else if (oreRobots < maxOres && ore - oreReserve >= ore4Ore) {
                        oreRobots++
                        ore--
                        ore -= ore4Ore
                    }
                }

                ore += oreRobots
                clay += clayRobots
                obsidian += obsidianRobots
                geode += geodeRobots
            }
            return geode.also { println("$maxOres $maxClays $it") }
        }
        var sum = 0
        input.forEachIndexed { index, line ->
            val blueprint = Blueprint.parse(line)
            var maxNumberOfGeodes = 0
            for (maxOres in 1..5)
                for (maxClays in 1..5)
                    maxNumberOfGeodes = maxOf(maxNumberOfGeodes, calc(blueprint, maxOres, maxClays, time = 24))

            sum += maxNumberOfGeodes * (index + 1).also { println("$maxNumberOfGeodes") }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
//    val testInput2 = readInput("Day19_test2")
    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 33)

//    println(part2(testInput))
//    check(part2(testInput) == 58)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day19")
//    println(part1(input))
//    println(part2(input))
}
