import java.util.*

const val RIGHT = 0
const val DOWN = 1
const val LEFT = 2
const val UP = 3

const val VOID = ' '
const val OPEN = '.'
const val WALL = '#'

fun main() {
    fun part1(input: List<String>): Int {
        val maxRow = input.size - 3
        val maxCol = input.takeWhile { it.isNotBlank() }.maxOf { it.length } - 1
        val board = Array(maxRow + 3) { Array(maxCol + 3) { VOID } } // leave void border
        input.takeWhile { it.isNotBlank() }.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c == OPEN || c == WALL) board[row + 1][col + 1] = c
            }
        }
        val path = """(\d+|[LR])""".toRegex().findAll(input.last()) // 10R5L5R10L4R5L5
        var (row, col) = listOf(1, board[1].indexOf(OPEN))
        var facing = RIGHT
        fun moveToWall(steps: Int, rowDiff: Int, colDiff: Int) {
            repeat(steps) {
                var rowNew = row + rowDiff
                var colNew = col + colDiff
                // skip void
                while (board[rowNew][colNew] == VOID) {
                    rowNew = (board.size + rowNew + rowDiff) % board.size
                    colNew = (board[0].size + colNew + colDiff) % board[0].size
                }
                if (board[rowNew][colNew] == WALL) return
                else {
                    row = rowNew
                    col = colNew
                }
            }
        }
        fun move(steps: Int) {
            when(facing) {
                RIGHT -> moveToWall(steps, 0, 1)
                DOWN -> moveToWall(steps, 1, 0)
                LEFT -> moveToWall(steps, 0, -1)
                UP -> moveToWall(steps, -1, 0)
            }
        }
        path.forEach { turn ->
            when(turn.value) {
                "L" -> facing = (4 + facing - 1) % 4
                "R" -> facing = (4 + facing + 1) % 4
                else -> move(turn.value.toInt())
            }
        }

        return 1000 * (row) + 4 * (col) + facing
    }

    fun part2(input: List<String>): Int {
        return TODO("Provide the return value")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
//    val testInput2 = readInput("Day22_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
    check(part1(testInput) == 6032)

//    println(part2(testInput))
//    check(part2(testInput) == 301)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day22")
//    check(part1(input)) == 109094
//    check(part2(input)) ==
    println(part1(input))
//    println(part2(input))
}
