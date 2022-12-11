import java.util.*

data class Monkey(
    val items: MutableList<Long>,
    val op: Char,
    val num: String,
    val testNum: Long,
    val ifTrue: Int,
    val ifFalse: Int,
    var count: Long,
)

fun main() {
    fun parse(chunk: List<String>): Monkey {
        val list = chunk[1].removePrefix("  Starting items: ").split(", ").map { it.toLong() }
        val op = chunk[2].removePrefix("  Operation: new = old ").first()
        val num = chunk[2].removePrefix("  Operation: new = old ").substring(2)
        val testNum = chunk[3].removePrefix("  Test: divisible by ").toLong()
        val ifTrue = chunk[4].removePrefix("    If true: throw to monkey ").toInt()
        val ifFalse = chunk[5].removePrefix("    If false: throw to monkey ").toInt()
        return Monkey(list.toMutableList(), op, num, testNum, ifTrue, ifFalse, 0, )
    }

    fun processRound(monkeys: MutableList<Monkey>, divider: Int) {
        monkeys.forEach { monkey ->
            with(monkey) {
                while (items.isNotEmpty()) {
                    var item = monkey.items.removeAt(0)
                    val operand = if (num == "old") item else num.toLong()
                    when(op) {
                        '*' -> item *= operand
                        '+' -> item += operand
                        else -> error("bad op $op")
                    }

                    item /= divider

                    (if (item % testNum == 0L) monkeys[ifTrue] else monkeys[ifFalse]).items.add(item)

                    count++
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()

        input.chunked(7).forEach { chunk ->
            val monkey = parse(chunk)
            monkeys.add(monkey)
        }
        repeat(20) {
            processRound(monkeys, 3)
        }

//        println(monkeys)

        return monkeys.map { it.count }.sortedDescending().take(2).reduce { acc, elem -> acc * elem }
    }

    fun part2(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()

        input.chunked(7).forEach { chunk ->
            val monkey = parse(chunk)
            monkeys.add(monkey)
        }
        repeat(10_000) {
            processRound(monkeys, 1)
        }

//        println(monkeys)

        return monkeys.map { it.count }.sortedDescending().take(2).reduce { acc, elem -> acc * elem }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    println(part1(testInput))
    check(part1(testInput) == 10605L)
    println(part2(testInput))
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
//    println(part2(input))
}
