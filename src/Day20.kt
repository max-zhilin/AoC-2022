import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { it.toInt() }
        val positions = IntArray(input.size) { it }
        val lastIndex = input.lastIndex
        fun move(from: Int, to: Int) {
            // 2 5 3 7 1 - from 1 to 3 -
            // 2 3 7 5 1
            // 0 1 2 3 4
            // 0 3 1 2 4

            // 2 5 3 7 0 - from 3 to 1
            // 2 7 5 3 0
            // 0 1 2 3 4
            // 0 2 3 1 4

            // 2 5 3 7 1 - 1 from 4 to 1 - (pos[i] + n) % size + 1
            // 2 1 3 7 5
            // 0 1 2 3 4

            // 2 5 3 7 10 - 10 from 4 to 1 - (pos[i] + n - 1) % (size - 1) + 1
            // 2 5 3 7 _ 2 5 3 7  2 5 3 7  2 5 (10) 3 7
            // 0 1 2 3 4
            if (from < to) {
                for (i in 0..lastIndex)
                    if (positions[i] in from + 1..to) positions[i]--
            } else {
                for (i in 0..lastIndex)
                    if (positions[i] in to..from - 1) positions[i]++
            }
        }
        numbers.forEachIndexed { index, num ->
            val from = positions[index]
            val to = if (num == 0) from
            else if (num > 0) (from + (num - 1)) % lastIndex + 1
//            else lastIndex + (from - lastIndex + (num + 1)) % lastIndex - 1
            else lastIndex + (from - lastIndex + num) % lastIndex
            move(from, to)
            positions[index] = to
//            val sorted = (0..lastIndex).associate { positions[it] to numbers[it] }.toSortedMap()
//            println(sorted.toList().joinToString { it.second.toString() })
        }
        var sum = 0
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 1000) % (lastIndex + 1))]
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 2000) % (lastIndex + 1))]
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 3000) % (lastIndex + 1))]

        return sum
    }

    fun part2(input: List<String>, key: Long): Long {
        val numbers = input.map { it.toLong() * key }
        val positions = LongArray(input.size) { it.toLong() }
        val lastIndex = input.lastIndex
        fun move(from: Long, to: Long) {
            if (from < to) {
                for (i in 0..lastIndex)
                    if (positions[i] in from + 1..to) positions[i]--
            } else {
                for (i in 0..lastIndex)
                    if (positions[i] in to..from - 1) positions[i]++
            }
        }
        repeat(10) {
            numbers.forEachIndexed { index, num ->
                val from = positions[index]
                val to = if (num == 0L) from
                else if (num > 0) 1 + (from - 1 + num) % lastIndex
//            else lastIndex + (from - lastIndex + (num + 1)) % lastIndex - 1
                else lastIndex + (from - lastIndex + num) % lastIndex
                move(from, to)
                positions[index] = to
//            val sorted = (0..lastIndex).associate { positions[it] to numbers[it] }.toSortedMap()
//            println(sorted.toList().joinToString { it.second.toString() })
            }
        }
        var sum = 0L
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 1000) % (lastIndex + 1))]
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 2000) % (lastIndex + 1))]
        sum += numbers[positions.indexOf((positions[numbers.indexOf(0)] + 3000) % (lastIndex + 1))]

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
//    val testInput2 = readInput("Day20_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
    check(part1(testInput) == 3)

//    println(part2(testInput, 811589153))
    check(part2(testInput, 811589153) == 1623178306L)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day20")
//    check(part2(input, 811589153)) == 656575624777L
//    println(part1(input))
    println(part2(input, 811589153))
}
