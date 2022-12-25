import java.util.*

const val RIGHT = 0
const val DOWN = 1
const val LEFT = 2
const val UP = 3

const val VOID = ' '
const val OPEN = '.'
const val WALL = '#'

fun transpose(side: Array<CharArray>): Array<CharArray> {
    for (i in 1..side.lastIndex) for (j in 0..i) {
        side[i][j] = side[j][i].also { side[j][i] = side[i][j] }
    }
    return side
}
fun rotateClockwise(side: Array<CharArray>): Array<CharArray> {
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

    fun part2(input: List<String>, size: Int, inputNet: List<String>): Int {
        val cube = Array(6) { Array(size) { CharArray(size) { OPEN } } }
        val rotation = IntArray(6)
        val maxPos = size - 1
        inputNet.forEach { chunk ->
            val side = chunk[0] - 'A'
            val rowFactor = chunk[1].digitToInt()
            val colFactor = chunk[2].digitToInt()
            val rotationOfSide = chunk[3].digitToInt()
            for (row in 0 until  size) for (col in 0 until size)
                cube[side][row][col] = input[row + rowFactor * size][col + colFactor * size]
            repeat(4 - rotationOfSide) {
                cube[side] = rotateClockwise(cube[side])
            }
            rotation[side] = rotationOfSide
        }
        val path = """(\d+|[LR])""".toRegex().findAll(input.last()) // 10R5L5R10L4R5L5
        var row = 0; var col = 0
        var side = 0
        var facing = RIGHT
        fun move(steps: Int) {
            repeat(steps) {
                var (rowNew, colNew) = when(facing) {
                    RIGHT -> listOf(row, col + 1)
                    DOWN -> listOf(row + 1, col)
                    LEFT -> listOf(row, col - 1)
                    UP -> listOf(row - 1, col)
                    else -> error("facing $facing")
                }
                var sideNew = side
                var facingNew = facing
                if (rowNew == -1) {
                    sideNew = (6 + side - 1) % 6
                    if (sideNew % 2 == 0) {
                        rowNew = maxPos - colNew
                        colNew = maxPos
                        facingNew = LEFT
                    } else {
                        rowNew = colNew
                        colNew = 0
                        facingNew = RIGHT
                    }
                }
                if (rowNew == maxPos + 1) {
                    sideNew = (6 + side + 2) % 6
                    if (sideNew % 2 == 0) {
                        rowNew = maxPos - colNew
                        colNew = 0
                        facingNew = RIGHT
                    } else {
                        rowNew = colNew
                        colNew = maxPos
                        facingNew = LEFT
                    }
                }
                if (colNew == -1)
                    if (side % 2 == 0) {
                        sideNew = (6 + side - 2) % 6
                        colNew = maxPos - rowNew
                        rowNew = maxPos
                        facingNew = UP
                    } else {
                        sideNew = (6 + side + 1) % 6
                        colNew = rowNew
                        rowNew = 0
                        facingNew = DOWN
                    }
                if (colNew == maxPos + 1)
                    if (side % 2 == 0) {
                        sideNew = (6 + side + 1) % 6
                        colNew = maxPos - rowNew
                        rowNew = 0
                        facingNew = DOWN
                    } else {
                        sideNew = (6 + side - 2) % 6
                        colNew = rowNew
                        rowNew = maxPos
                        facingNew = UP
                    }

                if (cube[sideNew][rowNew][colNew] == WALL) return
                else {
                    row = rowNew
                    col = colNew
                    side = sideNew
                    facing = facingNew
                }
            }
        }
        fun net(): Array<CharArray> {
            val maxRow = input.size - 3
            val maxCol = input.takeWhile { it.isNotBlank() }.maxOf { it.length } - 1
            val board = Array(maxRow + 1) { CharArray(maxCol + 1) { VOID } }
            for ((i, cubeFace) in cube.withIndex()) {
                var face = cubeFace.map { it.clone() }.toTypedArray()
                if (i == side) face[row][col] = 'O'
                val chunk = inputNet.first { it.first() == 'A' + i }
                val rowFactor = chunk[1].digitToInt()
                val colFactor = chunk[2].digitToInt()
                val rotationOfSide = chunk[3].digitToInt()
                repeat(rotationOfSide) {
                    face = rotateClockwise(face)
                }
                for (r in 0 until  size) for (c in 0 until size)
                    board[r + rowFactor * size][c + colFactor * size] = face[r][c]
            }
            return board
        }
        fun printBoard() {
            val board = net()
            val r = board.indexOfFirst { it.contains('O') }
            val c = board[r].indexOf('O')
            board[r][c] = ">v<^"[(facing + rotation[side]) % 4]
            println(board.joinToString("\n") { it.joinToString("") })
            println()
        }
        printBoard()
        path.forEach { turn ->
            when(turn.value) {
                "L" -> facing = (4 + facing - 1) % 4
                "R" -> facing = (4 + facing + 1) % 4
                else -> move(turn.value.toInt())
            }
            printBoard()
        }

        val board = net()
        val r = board.indexOfFirst { it.contains('O') }
        val c = board[r].indexOf('O')

        return 1000 * (r + 1) + 4 * (c + 1) + (facing + rotation[side]) % 4
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
//    val testInput2 = readInput("Day22_test2")
//    println(part1(testInput))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 6032)

//    println(part2(testInput, 4, "A020 F101 E112 C121 D220 B231 ".chunked(5))) // cube row col rot

    check(part2(testInput, 4, "A020 F101 E112 C121 D220 B231 ".chunked(5)) == 5031)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day22")
//    check(part1(input)) ==
//    check(part2(input)) == 53324
//    println(part1(input))
    println(part2(input, 50, "A010 B023 C111 E201 D210 F300 ".chunked(5)))
}
