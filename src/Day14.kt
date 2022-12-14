import java.util.*

const val air = ' '
const val rock = '#'
const val sand = '*'
fun placeRocks(cave: Array<CharArray>, line: String): Int {
    var maxRow = 0
    val pairs = line.split(" -> ")
    var (prevCol, prevRow) = listOf(-1, -1)
    pairs.forEach { pair ->
        val scanner = Scanner(pair)
        var (col, row) = scanner.next().split(",").map { it.toInt() }
//            col -= 490
        if (prevCol != -1) {
            if (col == prevCol) {
                for (i in minOf(row, prevRow)..maxOf(row, prevRow)) cave[i][col] = rock
            }
            if (row == prevRow) {
                for (i in minOf(col, prevCol)..maxOf(col, prevCol)) cave[row][i] = rock
            }
        }
        prevCol = col
        prevRow = row
        maxRow = maxOf(maxRow, row)
    }
    return maxRow
}
fun moveSand(cave: Array<CharArray>, rc: Pair<Int, Int>): Pair<Int, Int> {
    if (cave[rc.first + 1][rc.second] == air) return Pair(rc.first + 1, rc.second)
    if (cave[rc.first + 1][rc.second - 1] == air) return Pair(rc.first + 1, rc.second - 1)
    if (cave[rc.first + 1][rc.second + 1] == air) return Pair(rc.first + 1, rc.second + 1)
    return rc
}
fun main() {
    fun part1(input: List<String>): Int {
        val (rows, cols) = listOf(1000, 1000)
        val cave = Array(rows) { CharArray(cols) { air } }

        input.forEach {
            placeRocks(cave, it)
        }


        var sum = 0
        var gone = false
        while (!gone) {
            var coords = Pair(0, 500) // row, col
            while (true) {
                if (coords.first in 0 until rows - 1 && coords.second in 1 until cols - 1) {
                    val newCoords = moveSand(cave, coords)
                    if (newCoords == coords) {
                        cave[coords.first][coords.second] = sand
                        sum++
                        break
                    }
                    coords = newCoords
                } else {
                    gone = true
                    break
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val (rows, cols) = listOf(1000, 1000)
        val cave = Array(rows) { CharArray(cols) { air } }

        var maxRow = 0
        input.forEach {
            maxRow = maxOf(maxRow, placeRocks(cave, it))
        }
        val floor = maxRow + 2
        placeRocks(cave, "0,$floor -> ${cols - 1},$floor")

        var sum = 0
        var full = false
        while (!full) {
            var coords = Pair(0, 500) // row, col
            while (true) {
                val newCoords = moveSand(cave, coords)
                if (newCoords == Pair(0, 500)) {
                    sum++
                    full = true
                    break
                }
                if (newCoords == coords) {
                    cave[coords.first][coords.second] = sand
                    sum++
                    break
                }
                coords = newCoords
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    println(part1(testInput))
//    check(part1(testInput) == 24)
    println(part2(testInput))
    check(part2(testInput) == 93)

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}
