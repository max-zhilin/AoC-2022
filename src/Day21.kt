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
        input.forEach { line ->
            parse(line)
        }

        fun solve(first: Long, op: Operation, second: Long): Long {
            return when(op) {
                Operation.PLUS -> first + second
                Operation.MINUS -> first - second
                Operation.MUL -> first * second
                Operation.DIV -> first / second
            }
        }
        fun calc(expr: Expression): Long? {
            with(expr) {
                first = numbers[firstName] ?: if (firstName == "humn") null else calc( ops[firstName]!!)
                second = numbers[secondName] ?: if (secondName == "humn") null else calc( ops[secondName]!!)
                if (first != null && second != null)
                    return solve(first!!, op, second!!).also { numbers[name] = it }
                else
                    return null
            }
        }
        fun printExpr(expr: Expression) {
            with(expr) {
                if (first != null) print(first)
                else {
                    if (firstName == "humn") print("X")
//                    val firstExpr = ops[firstName]
//                    if (firstExpr == null) print("X")
                    else {
                        print("(")
                        printExpr(ops[firstName]!!)
                        print(")")
                    }
                }
                print(op.c)
                if (second != null) print(second)
                else {
                    if (secondName == "humn") print("X")
//                    val secondExpr = ops[secondName]
//                    if (secondExpr == null) print("X")
                    else {
                        print("(")
                        printExpr(ops[secondName]!!)
                        print(")")
                    }
                }
            }
        }
        val left = ops[ops["root"]?.firstName]!!
        val right = ops[ops["root"]?.secondName]!!
////        for (i in 0 .. (Int.MAX_VALUE / 10).toLong()) {
//        for (i in 0 downTo (-Int.MAX_VALUE / 10).toLong()) {
//            numbers["humn"] = i
//            if (calc(left) == calc(right)) return i
//        }
        numbers.remove("humn")
        println(calc(left))
        println(calc(right))

        printExpr(left)
        println()
        // with help of cite
        // https://math.semestr.ru/math/expand.php
        // and WolframAlpha
        // https://www.wolframalpha.com/input?i=%5Cfrac%7B7511588713779448%7D%7B135%7D-%5Cfrac%7B76636%5Ccdot+x%7D%7B6075%7D%3D6745394553620
        numbers["humn"] = 3876027196185
        println(calc(left)) // check equality
        return TODO("Provide the return value")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
//    val testInput2 = readInput("Day21_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 152L)

//    println(part2(testInput))
//    check(part2(testInput) == 301)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day21")
//    check(part1(input)) == 62386792426088
//    check(part2(input)) ==
//    println(part1(input))
    println(part2(input))
}
