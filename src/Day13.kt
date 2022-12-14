data class Line(var s: String) {
    var pos = 0
    val c: Char
        get() = s[pos]
    fun isDigit() = c.isDigit()
    fun closed() = pos == s.length
    operator fun inc(): Line {
        pos++
        return this
    }
    fun addList() {
        var end = pos
        do {
            end++
        } while (s[end].isDigit())
        s = s.substring(0, pos) + "[" + s.substring(pos, end) + "]" + s.substring(end)
        //pos++
    }
    fun nextInt(): Int {
        var end = pos
        do {
            end++
        } while (s[end].isDigit())
        return s.substring(pos, end).toInt().also { pos = end }
    }
}

fun smaller(s1: String, s2: String): Boolean {
    var (a, b) = listOf(Line(s1), Line(s2))
    while (true) {
        if (a.closed()) return true
        else if (b.closed()) return false

        if (a.c == ']' && b.c != ']') return true
        else if (b.c == ']' && a.c != ']') return false
        else if (a.c == ']' && b.c == ']') {
            a++
            b++
            continue
        }

        if (a.c == '[' && b.isDigit()) b.addList()
        if (b.c == '[' && a.isDigit()) a.addList()

        if (a.c == ',' && b.c != ',' || a.c != ',' && b.c == ',') error("bad tokens ${a.c} ${b.c}")
        if (a.c == ',' && b.c == ',') {
            a++
            b++
            continue
        }

        if (a.c == '[' && b.c == '[') {
            a++
            b++
            continue
        }

        if (!a.isDigit() || !b.isDigit()) error("not digit ${a.c} ${b.c}")
        val (a1, b1) = listOf(a.nextInt(), b.nextInt())
        if (a1 < b1) return true
        else if (a1 > b1) return false
    }
}


fun main() {
    fun part1(input: List<String>): Int {

        var sum = 0
//        var test = Line("asf123,")
//        test++
//        test++
//        test++
//        println(test.pos)
//        test.addList()
//        println(test.s)
//        println(test.pos)
//        test++
//        println(test.nextInt())
//        println(test.pos)
        input.chunked(3).forEachIndexed { index, strings ->
            if (smaller(strings[0], strings[1])) sum += index + 1
//            else println(strings[0] + "-" + strings[1])
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val myComparator =  Comparator<String> { a, b ->
            if (smaller(a, b)) -1 else 1
        }
        val list = input.filter { it.isNotBlank() }.toMutableList()
        list.add("[[2]]")
        list.add("[[6]]")
        val sorted = list.sortedWith(myComparator)
        val pos2 = sorted.indexOf("[[2]]") + 1
        val pos6 = sorted.indexOf("[[6]]") + 1

        return pos2 * pos6
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    println(part1(testInput))
    check(part1(testInput) == 13)
    println(part2(testInput))
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
