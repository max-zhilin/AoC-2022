
var cycle = 0
var x = 1

fun process(ticks: Int, operand: Int): Int {
    var result = 0
    repeat(ticks) {
        cycle++
        if ((cycle - 20) % 40 == 0) {
            result += x * cycle
        }
        draw()
    }
    x += operand
    return result
}
fun processDraw(ticks: Int, operand: Int) {
    repeat(ticks) {
        cycle++
        draw()
    }
    x += operand
}

fun draw() {
    val pos = (cycle - 1) % 40
    if (pos in x - 1..x + 1) print("#") else print(".")
    if (pos == 39) println()
}

fun main() {
    fun part1(input: List<String>): Int {
        cycle = 0
        x = 1

        var sum = 0

        input.forEach { line ->
            val op = line.substring(0, 4)
            when (op) {
                "noop" -> sum += process(1, 0)
                "addx" -> sum += process(2, line.substring(5).toInt())
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        cycle = 0
        x = 1

        var sum = 0

        input.forEach { line ->
            val op = line.substring(0, 4)
            when (op) {
                "noop" -> processDraw(1, 0)
                "addx" -> processDraw(2, line.substring(5).toInt())
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println(part1(testInput))
    check(part1(testInput) == 13140)
//    println(part2(testInput))
//    check(part2(testInput) == 36)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
