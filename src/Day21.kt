import java.util.*

enum class Operation(val c: Char) {
    PLUS('+'),
    MINUS('-'),
    MUL('*'),
    DIV('/');
    companion object {
        fun parse(c: Char): Operation {
            return when(c) {
                '+' -> PLUS
                '-' -> MINUS
                '*' -> MUL
                '/' -> DIV
                else -> error("bad op $c")
            }
        }
    }
}

data class Expression(val name: String, val firstName: String, val op: Operation, val secondName: String) {
    var first: Long? = null
    var second: Long? = null
}

fun main() {
    fun part1(input: List<String>): Long {
        val numbers = mutableMapOf<String, Long>()
        val ops = mutableListOf<Expression>()
        fun parse(s: String) {
            // lgvd: ljgn * ptdq
            // hmdt: 32
            val regex = """(\w+): (\d+)?(?:(\w+) ([+\-*/]) (\w+))?""".toRegex()

            val (name, number, firstName, op, secondName) = regex.matchEntire(s)//?.groupValues
                //?.map { it.toInt() }
                ?.destructured
                ?: error("Bad input '$s'")
            if (number.isNotEmpty())
                numbers[name] = number.toLong()
            else
                ops.add(Expression(name, firstName, Operation.parse(op.first()), secondName))
        }
        fun solve(first: Long, op: Operation, second: Long): Long {
            return when(op) {
                Operation.PLUS -> first + second
                Operation.MINUS -> first - second
                Operation.MUL -> first * second
                Operation.DIV -> first / second
            }
        }
        input.forEach { line ->
            parse(line)
        }

        while (true) {
            val itr = ops.iterator()
            
            while (itr.hasNext()) {
                with(itr.next()) {
                    if (first == null) {
                        first = numbers[firstName]
                    }
                    if (second == null) {
                        second = numbers[secondName]
                    }
                    if (first != null && second != null) {
                        val solvedNumber = solve(first!!, op, second!!)
                        numbers[name] = solvedNumber
                        if (name == "root") return numbers[name]!!
                        else itr.remove()
                    }
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Long {
        val numbers = mutableMapOf<String, Long>()
        val ops = mutableMapOf<String, Expression>()
        fun parse(s: String) {
            // lgvd: ljgn * ptdq
            // hmdt: 32
            val regex = """(\w+): (\d+)?(?:(\w+) ([+\-*/]) (\w+))?""".toRegex()

            val (name, number, first, op, second) = regex.matchEntire(s)//?.groupValues
                //?.map { it.toInt() }
                ?.destructured
                ?: error("Bad input '$s'")
            if (number.isNotEmpty())
                numbers[name] = number.toLong()
            else
                ops[name] = Expression(name, first, Operation.parse(op.first()), second)
        }
        fun solve(first: Long, op: Operation, second: Long): Long {
            return when(op) {
                Operation.PLUS -> first + second
                Operation.MINUS -> first - second
                Operation.MUL -> first * second
                Operation.DIV -> first / second
            }
        }
        fun calc(expr: Expression): Long {
            with(expr) {
                first = numbers[firstName] ?: calc(ops[firstName]!!)
                second = numbers[secondName] ?: calc(ops[secondName]!!)
                return solve(first!!, op, second!!)
            }
        }
        input.forEach { line ->
            parse(line)
        }

        val left = ops[ops["root"]?.firstName]!!
        val right = ops[ops["root"]?.secondName]!!
        for (i in 0 downTo (-Int.MAX_VALUE / 10000).toLong()) {
            numbers["humn"] = i
            if (calc(left) == calc(right)) return i
        }
        return TODO("Provide the return value")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
//    val testInput2 = readInput("Day21_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 152L)

    println(part2(testInput))
//    check(part2(testInput) == 301)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day21")
//    check(part1(input)) == 62386792426088
//    check(part2(input)) ==
//    println(part1(input))
    println(part2(input))
}
