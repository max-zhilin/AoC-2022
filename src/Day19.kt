import java.util.*

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
        var sum = 0
        input.forEachIndexed { index, line ->
            val blueprint = Blueprint.parse(line)
            fun calc(time: Int, oreRobots: Int, clayRobots: Int, obsidianRobots: Int, geodeRobots: Int,
                     ore: Int, clay: Int, obsidian: Int): Int {
                var geode = 0
                with(blueprint) {
                    if (time in 1..2)
                        return geodeRobots * time + if (obsidian >= obsidian4Geode && ore >= ore4Geode) time - 1 else 0

                    if (obsidian >= obsidian4Geode && ore >= ore4Geode)
                        geode = maxOf(geode, calc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots + 1,
                            ore + oreRobots - ore4Geode, clay + clayRobots, obsidian + obsidianRobots - obsidian4Geode))
                    else if (clay >= clay4Obsidian && ore >= ore4Obsidian)
                        geode = maxOf(geode, calc(time - 1, oreRobots, clayRobots, obsidianRobots + 1, geodeRobots,
                            ore + oreRobots - ore4Obsidian, clay + clayRobots - clay4Obsidian, obsidian + obsidianRobots))
                    else if (ore >= ore4Clay)
                        geode = maxOf(geode, calc(time - 1, oreRobots, clayRobots + 1, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Clay, clay + clayRobots, obsidian + obsidianRobots))
                    if (ore >= ore4Ore)
                        geode = maxOf(geode, calc(time - 1, oreRobots + 1, clayRobots, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Ore, clay + clayRobots, obsidian + obsidianRobots))

                    geode = maxOf(geode, calc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots,
                        ore + oreRobots, clay + clayRobots, obsidian + obsidianRobots))

                }
                return geodeRobots + geode
            }
            var maxNumberOfGeodes = calc(time = 24, 1, 0, 0, 0, 0, 0, 0)
//            var maxNumberOfGeodes = calc(time = 17, 1, 3, 0, 0, 1, 6, 0)

            sum += maxNumberOfGeodes * (index + 1).also { println("$maxNumberOfGeodes") }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 1
//        input.forEachIndexed { index, line ->
        input.take(3).forEach { line ->
            val blueprint = Blueprint.parse(line)
            val (maxOreRobots, maxClayRobots, maxObsidianRobots) = with(blueprint) {
                listOf(maxOf(ore4Ore, ore4Clay, ore4Obsidian, ore4Geode),
                    clay4Obsidian ,// 2 + 1,
                    obsidian4Geode // 2 + 1
                )
            }
            data class Params(val oreRobots: Int, val clayRobots: Int, val obsidianRobots: Int, val geodeRobots: Int, val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)
            val preset = mutableSetOf<Params>()
            fun precalc(time: Int, oreRobots: Int, clayRobots: Int, obsidianRobots: Int, geodeRobots: Int, ore: Int, clay: Int, obsidian: Int, geode: Int) {
                with(blueprint) {
                    if (time == 0) {
                        preset.add(Params(oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geode))
                        return
                    }

                    if (obsidian >= obsidian4Geode && ore >= ore4Geode)
                        precalc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots + 1,
                            ore + oreRobots - ore4Geode, clay + clayRobots, obsidian + obsidianRobots - obsidian4Geode, geode + geodeRobots)
                    if (obsidianRobots < maxObsidianRobots && clay >= clay4Obsidian && ore >= ore4Obsidian)
                        precalc(time - 1, oreRobots, clayRobots, obsidianRobots + 1, geodeRobots,
                            ore + oreRobots - ore4Obsidian, clay + clayRobots - clay4Obsidian, obsidian + obsidianRobots, geode + geodeRobots)
                    if (clayRobots < maxClayRobots && ore >= ore4Clay)
                        precalc(time - 1, oreRobots, clayRobots + 1, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Clay, clay + clayRobots, obsidian + obsidianRobots, geode + geodeRobots)
                    if (oreRobots < maxOreRobots && ore >= ore4Ore)
                        precalc(time - 1, oreRobots + 1, clayRobots, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Ore, clay + clayRobots, obsidian + obsidianRobots, geode + geodeRobots)

                    precalc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots,
                        ore + oreRobots, clay + clayRobots, obsidian + obsidianRobots, geode + geodeRobots)
                }
            }
            var maxOre = 0; var maxClay = 0; var maxObsidian = 0
            fun calc(time: Int, oreRobots: Int, clayRobots: Int, obsidianRobots: Int, geodeRobots: Int,
                     ore: Int, clay: Int, obsidian: Int): Int {
                var geodes = 0
//                maxOre = maxOf(maxOre, ore); maxClay = maxOf(maxClay, clay); maxObsidian = maxOf(maxObsidian, obsidian)
                with(blueprint) {
                    if (time in 1..2)
                        return geodeRobots * time + if (obsidian >= obsidian4Geode && ore >= ore4Geode) time - 1 else 0
                    if (time == 3)
                        return geodeRobots * time + if (obsidian >= obsidian4Geode && ore >= ore4Geode)
                            if (obsidian - obsidian4Geode + obsidianRobots >= obsidian4Geode && ore - ore4Geode + oreRobots >= ore4Geode) 3 else 2
                        else if (obsidian + obsidianRobots >= obsidian4Geode && ore + oreRobots >= ore4Geode) 1 else 0

                    if (obsidian >= obsidian4Geode && ore >= ore4Geode)
                        geodes = maxOf(geodes, calc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots + 1,
                            ore + oreRobots - ore4Geode, clay + clayRobots, obsidian + obsidianRobots - obsidian4Geode))
//                    else
                    if (
                        obsidianRobots < maxObsidianRobots &&
                        clay >= clay4Obsidian && ore >= ore4Obsidian)
                        geodes = maxOf(geodes, calc(time - 1, oreRobots, clayRobots, obsidianRobots + 1, geodeRobots,
                            ore + oreRobots - ore4Obsidian, clay + clayRobots - clay4Obsidian, obsidian + obsidianRobots))
                    if (
//                        time > 8 &&
                        clayRobots < maxClayRobots &&
                        ore >= ore4Clay)
                        geodes = maxOf(geodes, calc(time - 1, oreRobots, clayRobots + 1, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Clay, clay + clayRobots, obsidian + obsidianRobots))
                    if (
//                        time > 8 &&
                        oreRobots < maxOreRobots &&
                        ore >= ore4Ore)
                        geodes = maxOf(geodes, calc(time - 1, oreRobots + 1, clayRobots, obsidianRobots, geodeRobots,
                            ore + oreRobots - ore4Ore, clay + clayRobots, obsidian + obsidianRobots))

                    if (
//                        time > 5 &&
//                        ore < maxOreRobots * 4 && clay < maxClayRobots * 4 && //obsidian < maxObsidianRobots * 4 &&
                        true)
                        geodes = maxOf(geodes, calc(time - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots,
                            ore + oreRobots, clay + clayRobots, obsidian + obsidianRobots))

                }
                return geodeRobots + geodes
            }
            val precalcTime = 22
            precalc(precalcTime, 1, 0, 0, 0, 0, 0, 0, 0)
            println("set size ${preset.size}")
            var count = 0
//            sum += (index + 1) * preset.maxOf { params ->
            sum *= preset.maxOf { params ->
                with(params) {
                    count++
                    if (count % 10000 == 0) print("*")
                    geode + calc(time = 32 - precalcTime, oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian)
                }
            }.also { println(); println(it) }
//            println("max $maxOre $maxClay $maxObsidian")
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    val testInput2 = readInput("Day19_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 33)

//    println(part2(testInput))
//    println(part2(testInput2))
//    check(part2(testInput) == 58)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day19")
//    println(part1(input)) // = 1346
    println(part2(input)) // = 7644
}
