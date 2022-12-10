import kotlin.math.abs

data class Coord(var x: Int, var y: Int)

const val dim = 1000

val field = Array(dim) { ByteArray(dim) }
val rope = Array(10) { Coord(dim / 2, dim / 2) }


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
}

fun moveRopeHead(h: Coord, dir: Char) {
    when (dir) {
        'U' -> h.y++
        'D' -> h.y--
        'R' -> h.x++
        'L' -> h.x--
        else -> error("wrong dir $dir")
    }
}

fun pullRope(h: Coord, t: Coord) {
    val dx = h.x - t.x
    val dy = h.y - t.y
    if (dy == 0) t.x = t.x + (dx / 2)
    else if (dx == 0) t.y = t.y + (dy / 2)
    else if (abs(dy) > 1 && abs(dx) > 1) {
        t.x += dx / 2
        t.y = t.y + dy / 2
    } else {
        t.x = t.x + if (abs(dy) > 1) dx else dx / 2
        t.y = t.y + if (abs(dx) > 1) dy else dy / 2
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val field = Array(dim) { ByteArray(dim) }

        input.forEach { line ->
            val dir = line.first()
            val steps = line.substring(2).toInt()
            repeat(steps) {
                moveHead(dir)
                pullTail()
                field[tx][ty] = 1
            }
        }

        return field.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
//        val field = Array(dim) { ByteArray(dim) }
//        val rope = Array(10) { Coord(dim / 2, dim / 2) }

        input.forEach { line ->
            val dir = line.first()
            val steps = line.substring(2).toInt()
            repeat(steps) {
                moveRopeHead(rope[0], dir)
                for (i in 1..9) {
                    pullRope(rope[i - 1], rope[i])
                }
                field[rope[9].x][rope[9].y] = 1
            }
        }

        return field.sumOf { it.sum() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    println(part1(testInput))
//    check(part1(testInput) == 13)
//    println(part2(testInput))
//    check(part2(testInput) == 36)

    val input = readInput("Day09")
//    println(part1(input))
    println(part2(input))
}
