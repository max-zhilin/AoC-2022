fun main() {
    fun contains(a: String, b: String): Boolean {
        val (ax, ay) = a.split("-").map { it.toInt() }
        val (bx, by) = b.split("-").map { it.toInt() }
        if (ax <= bx && ay >= by) return true
        if (bx <= ax && by >= ay) return true
        return false
    }

    fun overlaps(a: String, b: String): Boolean {
        val (ax, ay) = a.split("-").map { it.toInt() }
        val (bx, by) = b.split("-").map { it.toInt() }
        if (ax in  bx..by) return true
        if (ay in  bx..by) return true
        if (bx in  ax..ay) return true
        if (by in  ax..ay) return true
        return false
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val (left, right) = it.split(",")
            if (contains(left, right)) sum++
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val (left, right) = it.split(",")
            if (overlaps(left, right)) sum++
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
//    println(part1(testInput))
    check(part1(testInput) == 2)
//    println(part2(testInput))
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
