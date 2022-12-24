import java.util.*

const val RIGHT = 0
const val DOWN = 1
const val LEFT = 2
const val UP = 3

const val VOID = ' '
const val OPEN = '.'
const val WALL = '#'

fun transpose(side: Array<Array<Char>>): Array<Array<Char>> {
    for (i in 1..side.lastIndex) for (j in 0..i) {
        side[i][j] = side[j][i].also { side[j][i] = side[i][j] }
    }
    return side
}
fun rotateClockwise(side: Array<Array<Char>>): Array<Array<Char>> {
    return transpose(side.reversed().toTypedArray())
}

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

    fun part2(input: List<String>, size: Int, net: List<String>): Int {
        val cubes = Array(6) { Array(size) { Array(size) { OPEN } } }
        val rotation = IntArray(6)
        net.forEach { chunk ->
            val side = chunk[0] - 'A'
            val rowFactor = chunk[1].digitToInt()
            val colFactor = chunk[2].digitToInt()
            val rotationOfSide = chunk[3].digitToInt()
            for (row in 0 until  size) for (col in 0 until size)
                cubes[side][row][col] = input[row + rowFactor * size][col + colFactor * size]
            repeat(4 - rotationOfSide) {
                cubes[side] = rotateClockwise(cubes[side])
            }
            rotation[side] = rotationOfSide
        }
        val path = """(\d+|[LR])""".toRegex().findAll(input.last()) // 10R5L5R10L4R5L5
        var (row, col) = listOf(0, 0)
        var side = 0
        var facing = RIGHT
        fun moveToWall(steps: Int, rowDiff: Int, colDiff: Int) {
            repeat(steps) {
                var rowNew = row + rowDiff
                var colNew = col + colDiff
                // skip void
//                while (board[rowNew][colNew] == VOID) {
//                    rowNew = (board.size + rowNew + rowDiff) % board.size
//                    colNew = (board[0].size + colNew + colDiff) % board[0].size
//                }
//                if (board[rowNew][colNew] == WALL) return
//                else {
//                    row = rowNew
//                    col = colNew
//                }
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

        return 1000 * (row + 1) + 4 * (col + 1) + (facing + rotation[side]) % 4
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
//    val testInput2 = readInput("Day22_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 6032)

    println(part2(testInput, 4, "A020 F101 E112 C121 D220 B231 ".chunked(5))) // cube row col rot

//    check(part2(testInput) == 301)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day22")
//    check(part1(input)) == 109094
//    check(part2(input)) ==
//    println(part1(input))
//    println(part2(input))
}
