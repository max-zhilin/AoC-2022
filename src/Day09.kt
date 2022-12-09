import kotlin.math.abs

data class Coord(var x: Int, var y: Int)

const val dim = 1000

val field = Array(dim) { ByteArray(dim) }
val h = Coord(dim / 2, dim / 2)
val t = Coord(dim / 2, dim / 2)
var hx = dim / 2
var hy = dim / 2
var tx = dim / 2
var ty = dim / 2

fun moveHead(dir: Char) {
    when (dir) {
        'U' -> hy++
        'D' -> hy--
        'R' -> hx++
        'L' -> hx--
        else -> error("wrong dir $dir")
    }
}

fun pullTail() {
    val dx = hx - tx
    val dy = hy - ty
    if (dy == 0) tx += dx / 2
    else if (dx == 0) ty += dy / 2
    else {
        tx += if (abs(dy) > 1) dx else dx / 2
        ty += if (abs(dx) > 1) dy else dy / 2
    }

    field[tx][ty] = 1
}

fun main() {
    fun part1(input: List<String>): Int {

        input.forEach { line ->
            val dir = line.first()
            val steps = line.substring(2).toInt()
            repeat(steps) {
                moveHead(dir)
                pullTail()
            }
        }

        return field.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        var max = 0

        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    println(part1(testInput))
//    check(part1(testInput) == 13)
//    println(part2(testInput))
    check(part2(testInput) == 36)

    val input = readInput("Day09")
//    println(part1(input))
//    println(part2(input))
}
