import java.util.*

//const val RIGHT = 0
//const val DOWN = 1
//const val LEFT = 2
//const val UP = 3

//const val GND = '.'
//const val ELF = '#'

//const val N = 0
//const val S = 1
//const val W = 2
//const val E = 3

fun main() {
    fun part1(input: List<String>): String {
        fun convert(s: String): Long {
            var result = 0L
            s.forEach { c ->
                result *= 5
                result += when (c) {
                    '2' -> 2
                    '1' -> 1
                    '0' -> 0
                    '-' -> -1
                    '=' -> -2
                    else -> error("wrong char $c")
                }
            }
            return  result
        }
        var sum = 0L
        input.forEach { line ->
            sum += convert(line)
        }

        println(sum)

        var result = ""
        while (sum != 0L) {
            val frac = sum % 5
            // 10- = 24   4, 4, 0
            val digit = when (frac.toInt()) {
                4 -> { sum += 5; "-" }
                3 -> { sum += 5; "=" }
                2 -> "2"
                1 -> "1"
                0 -> "0"
                else -> error("wrong digit $frac")
            }
            result = digit + result
            sum /= 5
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return TODO("Return not defined")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
//    val testInput2 = readInput("Day25_test2")
    println(part1(testInput))
//    println(part1(testInput2))
//    check(part1(testInput2) == )
//    check(part1(testInput) == "2=-1=0") // 4890

//    println(part2(testInput))
//    check(part2(testInput) == 54)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day25")
//    check(part1(input)) == 3947
//    check(part2(input)) ==
    println(part1(input))
//    println(part2(input))
}
