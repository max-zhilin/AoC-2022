fun main() {
    fun part1(input: List<String>): Int {
        val list = mutableListOf<Int>()
        list.add(0)
        for (s in input) {
            if (s.isEmpty()) {
                list.add(0)
            } else {
                list[list.lastIndex]+= s.toInt()
            }
        }
        return list.max()
    }

    fun part2(input: List<String>): Int {
        val list = mutableListOf<Int>()
        list.add(0)
        for (s in input) {
            if (s.isEmpty()) {
                list.add(0)
            } else {
                list[list.lastIndex]+= s.toInt()
            }
        }
        list.sortDescending()
        return list.take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
