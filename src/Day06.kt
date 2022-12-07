fun main() {
    fun part1(input: List<String>): Int {

        var buf = ""
        input[0].forEachIndexed() { pos, c ->
            buf += c

            val set = mutableSetOf<Char>()
            buf.forEach { b ->
                set.add(b)
                if (set.size == 4) {
                    return pos + 1
                }
            }

            if (buf.length == 4) buf = buf.substring(1)
        }

//        println(tail)

//        var sum = ""
//        stacks.forEach {
//            val c = it.peek()
//            if (c.isLetter()) sum += c
//        }
        return 0
    }

    fun part2(input: List<String>): Int {

        var buf = ""
        input[0].forEachIndexed() { pos, c ->
            buf += c

            val set = mutableSetOf<Char>()
            buf.forEach { b ->
                set.add(b)
                if (set.size == 14) {
                    return pos + 1
                }
            }

            if (buf.length == 14) buf = buf.substring(1)
        }

//        println(tail)

//        var sum = ""
//        stacks.forEach {
//            val c = it.peek()
//            if (c.isLetter()) sum += c
//        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
//    println(part1(testInput))
    check(part1(testInput) == 7)
//    println(part2(testInput))
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
