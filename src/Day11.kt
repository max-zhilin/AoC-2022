
class Remainder(divisors: List<Int>, value: Int) {
    val map: MutableMap<Int, Int> = divisors.associateWith { 0 }.toMutableMap()
    var full: Long = 0
    init {
        add(value)
    }

    fun put(divisor: Int, value: Int) {
        if (map.containsKey(divisor))
            map[divisor] = value
        else error("no such key $divisor")
    }

    fun add(value: Int) {
        map.forEach { (divisor, reminder) ->
            this.put(divisor, (reminder + value) % divisor)
        }
        full += value
    }

    fun multiply(value: Int) {
        map.forEach { (divisor, reminder) ->
            this.put(divisor, (reminder * value) % divisor)
        }
        full *= value
    }

    fun square() {
        map.forEach { (divisor, reminder) ->
            this.put(divisor, (reminder * reminder) % divisor)
        }
        full *= full
    }

    fun value(divisor: Int): Int {
        if (map.containsKey(divisor))
            return map[divisor] ?: error("null")
        else error("no value for key $divisor")

    }
    fun isDividedBy(divisor: Int): Boolean {
        if (map.containsKey(divisor))
            return map[divisor] == 0
        else error("no such key $divisor")
    }

}
data class Monkey(
    val items: MutableList<Remainder>,
    val op: Char,
    val num: String,
    val divisor: Int,
    val ifTrue: Int,
    val ifFalse: Int,
    var count: Long,
)

fun main() {
    fun parse(chunk: List<String>, divisors: List<Int>): Monkey {
        val list = chunk[1].removePrefix("  Starting items: ").split(", ").map { Remainder(divisors, it.toInt()) }
        val op = chunk[2].removePrefix("  Operation: new = old ").first()
        val num = chunk[2].removePrefix("  Operation: new = old ").substring(2)
        val divisor = chunk[3].removePrefix("  Test: divisible by ").toInt()
        val ifTrue = chunk[4].removePrefix("    If true: throw to monkey ").toInt()
        val ifFalse = chunk[5].removePrefix("    If false: throw to monkey ").toInt()
        return Monkey(list.toMutableList(), op, num, divisor, ifTrue, ifFalse, 0, )
    }

    fun part1(input: List<String>): Int {
        fun processRound(monkeys: MutableList<Monkey>) {
            monkeys.forEach { monkey ->
                with(monkey) {
                    while (items.isNotEmpty()) {
                        val item = items.removeAt(0)
                        val operand = if (num == "old") item.value(Int.MAX_VALUE) else num.toInt()
                        when(op) {
                            '*' -> item.multiply(operand)
                            '+' -> item.add(operand)
                            else -> error("bad op $op")
                        }

                        item.put(Int.MAX_VALUE, item.value(Int.MAX_VALUE) / 3)
                        item.full /= 3

//                        (if (item.value(Int.MAX_VALUE) % divisor == 0) monkeys[ifTrue] else monkeys[ifFalse]).items.add(item)
                        (if (item.full % divisor.toLong() == 0L) monkeys[ifTrue] else monkeys[ifFalse]).items.add(item)

                        count++
                    }
                }
            }
        }

        val monkeys = mutableListOf<Monkey>()

        val divisors = listOf(Int.MAX_VALUE)

        input.chunked(7).forEach { chunk ->
            val monkey = parse(chunk, divisors)
            monkeys.add(monkey)
        }
        repeat(20) {
            processRound(monkeys)
        }

//        println(monkeys)

        return monkeys.map { it.count.toInt() }.sortedDescending().take(2).reduce { acc, elem -> acc * elem }
    }

    fun part2(input: List<String>): Long {
        fun processRound(monkeys: MutableList<Monkey>) {
            monkeys.forEachIndexed { index, monkey ->
                with(monkey) {
                    print("Monkey $index ($divisor$op$num) ")
                    while (items.isNotEmpty()) {
                        val item = items.removeAt(0)
                        print(item.value(divisor))

                        if (num == "old")
                            item.square()
                        else {
                            val operand = num.toInt()
                            when(op) {
                                '*' -> item.multiply(operand)
                                '+' -> item.add(operand)
                                else -> error("bad op $op")
                            }
                        }
                        print(op)
                        print(num.toIntOrNull() ?: "old")
                        print("=")
                        print(item.value(divisor))

//                    item /= divider
                        val test = item.isDividedBy(divisor)
//                        val test = item.full % divisor == 0L
                        (if (test) monkeys[ifTrue] else monkeys[ifFalse]).items.add(item)

                        count++

                        print("(${if (test) "YES" else "no"}->${if (test) ifTrue else ifFalse}), ")
                    }
                }
                println()
            }
        }

        val monkeys = mutableListOf<Monkey>()

        val divisors = input.chunked(7).map { chunk ->
            chunk[3].removePrefix("  Test: divisible by ").toInt()
        }

        input.chunked(7).forEach { chunk ->
            val monkey = parse(chunk, divisors)
            monkeys.add(monkey)
        }
        repeat(10_000) {
//            if (it == 1) println(monkeys.map { it.count })
//            if (it + 1 == 20) println(monkeys.map { it.count })
//            if (it + 1 % 1000 == 0) println(monkeys.map { it.count })
            println(monkeys.map { it.count })
            println("Round ${it + 1}")
            println(monkeys.map { it.items.map { it.full } })
            println(monkeys.map { it.items.map { it.map } }.joinToString("\n"))
            processRound(monkeys)
        }
        println(monkeys.map { it.count })
        println(monkeys.map { it.items.map { it.full } })

//        println(monkeys)

        return monkeys.map { it.count }.sortedDescending().take(2).reduce { acc, elem -> acc * elem }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    println(part1(testInput))
    check(part1(testInput) == 10605)
    //println(part2(testInput))
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))
}
