import java.util.*

//const val RIGHT = 0
//const val DOWN = 1
//const val LEFT = 2
//const val UP = 3

//const val GND = '.'
//const val ELF = '#'

//const val N = 0
//const val S = 1
//const val W = 2
//const val E = 3

const val DOT = '.'

fun main() {
    fun part1(input: List<String>): Int {
        val rows = input.size - 2
        val maxRow = rows - 1
        val cols = input[0].length - 2
        val maxCol = cols - 1
        val blizzards = Array(rows) { CharArray(cols) { DOT } }
        var now = Array(rows) { CharArray(cols) { DOT } }
        var next = Array(rows) { CharArray(cols) { DOT } }
        input.subList(1, input.size - 1).forEachIndexed { row, line ->
            line.substring(1, input[0].length - 1).forEachIndexed { col, char ->
                blizzards[row][col] = char
            }
        }
        fun free(time: Int, r: Int, c: Int): Boolean {
            if (r !in 0..maxRow) return false
            if (c !in 0..maxCol) return false
            // back = cycle - time % cycle
            return blizzards[r][(c + time) % cols] != '<' &&
                    blizzards[r][(c + cols - time % cols) % cols] != '>' &&
                    blizzards[(r + time) % rows][c] != '^' &&
                    blizzards[(r + rows - time % rows) % rows][c] != 'v'
        }

        for (i in 0..cols) {
            if (!free(i, 0, 0)) continue
            now[0][0] = ELF
            for (time in i..10_000) {
                for (r in 0..maxRow)
                    for (c in 0..maxCol)
                        if (now[r][c] == ELF) {
                            if (free(time + 1, r, c)) next[r][c] = ELF
                            if (free(time + 1, r, c + 1)) next[r][c + 1] = ELF
                            if (free(time + 1, r, c - 1)) next[r][c - 1] = ELF
                            if (free(time + 1, r + 1, c)) next[r + 1][c] = ELF
                            if (free(time + 1, r - 1, c)) next[r - 1][c] = ELF
                        }
                if (next[maxRow][maxCol] == ELF) return time + 2
                now = next
                next = Array(rows) { CharArray(cols) { DOT } }
            }
        }
        return TODO("Return not defined")
    }

    fun part2(input: List<String>): Int {
        val rows = input.size - 2
        val maxRow = rows - 1
        val cols = input[0].length - 2
        val maxCol = cols - 1
        val blizzards = Array(rows) { CharArray(cols) { DOT } }
        input.subList(1, input.size - 1).forEachIndexed { row, line ->
            line.substring(1, input[0].length - 1).forEachIndexed { col, char ->
                blizzards[row][col] = char
            }
        }
        fun free(time: Int, r: Int, c: Int): Boolean {
            if (r !in 0..maxRow) return false
            if (c !in 0..maxCol) return false
            // back = cycle - time % cycle
            return blizzards[r][(c + time) % cols] != '<' &&
                    blizzards[r][(c + cols - time % cols) % cols] != '>' &&
                    blizzards[(r + time) % rows][c] != '^' &&
                    blizzards[(r + rows - time % rows) % rows][c] != 'v'
        }
        fun travel(fromTime: Int, ar: Int, ac: Int, br: Int, bc: Int): Int {
            var now = Array(rows) { CharArray(cols) { DOT } }
            var next = Array(rows) { CharArray(cols) { DOT } }
            now[ar][ac] = ELF
            for (time in fromTime..10_000) {
                for (r in 0..maxRow)
                    for (c in 0..maxCol)
                        if (now[r][c] == ELF) {
                            if (free(time + 1, r, c)) next[r][c] = ELF
                            if (free(time + 1, r, c + 1)) next[r][c + 1] = ELF
                            if (free(time + 1, r, c - 1)) next[r][c - 1] = ELF
                            if (free(time + 1, r + 1, c)) next[r + 1][c] = ELF
                            if (free(time + 1, r - 1, c)) next[r - 1][c] = ELF
                        }
                if (next[br][bc] == ELF) return time + 2
                now = next
                next = Array(rows) { CharArray(cols) { DOT } }
            }
            return Int.MAX_VALUE
        }
        var firstTime = 305
        var secondTime = Int.MAX_VALUE
        for (time in firstTime..firstTime + 600) {
            if (!free(time, maxRow, maxCol)) continue
            val newTime = travel(time, maxRow, maxCol, 0, 0)
            secondTime = minOf(secondTime, newTime)
        }
        println("secondTime $secondTime")
        var thirdTime = Int.MAX_VALUE
        for (time in secondTime..secondTime + 600) {
            if (!free(time, maxRow, maxCol)) continue
            val newTime = travel(time, 0, 0, maxRow, maxCol)
            thirdTime = minOf(thirdTime, newTime)
//            println(newTime)
        }
        return if (thirdTime == Int.MAX_VALUE) TODO("Return not defined") else thirdTime
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
//    val testInput2 = readInput("Day24_test2")
//    println(part1(testInput))
//    println(part1(testInput2))
//    check(part1(testInput2) == )
//    check(part1(testInput) == 18)

//    println(part2(testInput))
//    check(part2(testInput) == 54)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day24")
//    check(part1(input)) == 3947
//    check(part2(input)) ==
//    println(part1(input))
    println(part2(input))
}
