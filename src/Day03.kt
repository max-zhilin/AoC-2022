fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val (left, right) = it.chunked(it.length / 2)
            loop@ for (i in left) {
                for (j in right) {
                    if (i == j) {
                        val priority = if (i < 'a') i - 'A' + 27 else i - 'a' + 1
                        sum += priority
                        break@loop
                    }
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val groups = input.chunked(3)
        groups.forEach {
            val (one, two, three) = it
            loop@ for (i in one) {
                for (j in two) {
                    for (k in three) {
                        if (i == j && j == k) {
                            val priority = if (i < 'a') i - 'A' + 27 else i - 'a' + 1
                            sum += priority
                            break@loop
                        }
                    }
                }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
//    println(part1(testInput))
    check(part1(testInput) == 157)
//    println(part2(testInput))
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
