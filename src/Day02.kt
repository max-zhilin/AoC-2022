fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val (first, second) = s.split(" ")
            val elf = first.first() - 'A' + 1
            val me = second.first() - 'X' + 1
            if (elf == 1 && me == 3) sum += 0
            else if (elf == 3 && me == 1) sum += 6
            else if (me > elf) sum += 6
            else if (elf == me) sum += 3
            else if (elf > me) sum += 0
            sum += me
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val (first, second) = s.split(" ")
            val elf = first.first() - 'A' + 1
            val win = second.first() - 'X'
            val me = when (win) {
                0 -> if (elf == 1) 3 else elf - 1
                1 -> elf
                else -> if (elf == 3) 1 else elf + 1
            }
            sum += me + win * 3
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
//    println(part1(testInput))
    check(part1(testInput) == 15+12+10)
//    println(part2(testInput))
    check(part2(testInput) == 12+16+10)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
