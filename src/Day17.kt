
const val movingRock = '*'

data class Pos(var row: Int, var col: Int)

fun Array<CharArray>.shiftLeft() {
    forEach { line ->
        for ((i, c) in line.withIndex())
            if (c == movingRock && (i > 0 && line[i - 1] == rock || i == 0))
                return
    }
    forEach { line ->
        for ((i, c) in line.withIndex())
            if (c == movingRock) {
                line[i - 1] = movingRock
                line[i] = air
            }
    }
}
fun Array<CharArray>.shiftRight() {
    forEach { line ->
        for ((i, c) in line.withIndex().reversed())
            if (c == movingRock && (i < line.lastIndex && line[i + 1] == rock || i == line.lastIndex))
                return
    }
    forEach { line ->
        for ((i, c) in line.withIndex().reversed())
            if (c == movingRock) {
                line[i + 1] = movingRock
                line[i] = air
            }
    }
}
fun Array<CharArray>.fall(): Boolean {
    for (row in 0 .. lastIndex)
        for ((i, c) in this[row].withIndex())
            if (c == movingRock && (row == 0 || this[row - 1][i] == rock))
                return false
    for (row in 1 .. lastIndex)
        for ((i, c) in this[row].withIndex())
            if (c == movingRock) {
                this[row - 1][i] = movingRock
                this[row][i] = air
            }
    return true
}
fun Array<CharArray>.freeze() {
    forEach { line ->
        for ((i, c) in line.withIndex())
            if (c == movingRock) line[i] = rock
    }
}
fun Array<CharArray>.height(oldHeight: Int): Int {
    for (row in oldHeight + 10 downTo oldHeight)
        for (c in this[row])
            if (c == rock) return row + 1
    return oldHeight
}
fun Array<CharArray>.fallInWindow(): Boolean {
    for (row in 0 .. lastIndex)
        for ((i, c) in this[row].withIndex())
            if (c == movingRock)
                if (row == 0) error("window too small")
                else if (this[row - 1][i] == rock)
                    return false
    for (row in 1 .. lastIndex)
        for ((i, c) in this[row].withIndex())
            if (c == movingRock) {
                this[row - 1][i] = movingRock
                this[row][i] = air
            }
    return true
}
fun Array<CharArray>.slide(diff: Int) {
    for (row in diff .. lastIndex)
        this[row - diff] = this[row]
}

fun main() {
    fun buildRocks(): Array<Array<String>> {
        return arrayOf(
            arrayOf(
                "****",
            ),
            arrayOf(
                " * ",
                "***",
                " * ",
            ),
            arrayOf(
                "  *",
                "  *",
                "***",
            ).reversedArray(),
            arrayOf(
                "*",
                "*",
                "*",
                "*",
            ),
            arrayOf(
                "**",
                "**",
            ),
        )
    }

    fun part1(input: List<String>, times: Int): Int {
        val (rows, cols) = listOf(4_000, 7)
        val chamber = Array(rows) { CharArray(cols) { air } }
        val rocks = buildRocks()
        val wind = input[0]
        var height = 0
        var counter = 0
        repeat(times) { time ->
            val rock = rocks[time % 5]
            val rockPosition = Pos(height + 3, 2)
            // draw rock
            for ((row, line) in rock.withIndex())
                for ((col, c) in line.withIndex())
                    chamber[row + rockPosition.row][col + rockPosition.col] = c

            do {
                when (wind[counter % wind.length]) {
                    '<' -> chamber.shiftLeft()
                    '>' -> chamber.shiftRight()
                }
                counter++
                val isSpace = chamber.fall()
            } while (isSpace)
            chamber.freeze()
            height = chamber.height(oldHeight = height)
        }

        return height
    }

    fun part2(input: List<String>, times: Int): Int {
        val (rows, cols) = listOf(1000, 7)
        val chamber = Array(rows) { CharArray(cols) { air } }
        val rocks = buildRocks()
        val wind = input[0]
        var height = 0
        var addition = 0
        var counter = 0
        var windowed = false
        repeat(times) { time ->
            if (height + 3 + 4 > rows) {
                windowed = true
                chamber.slide(7)
                height -= 7
                addition += 7
            }
            val rock = rocks[time % 5]
            val rockPosition = Pos(height + 3, 2)

            // draw rock
            for ((row, line) in rock.withIndex())
                for ((col, c) in line.withIndex())
                    chamber[row + rockPosition.row][col + rockPosition.col] = c

            do {
                when (wind[counter % wind.length]) {
                    '<' -> chamber.shiftLeft()
                    '>' -> chamber.shiftRight()
                }
                counter++
                val isSpace = if (windowed) chamber.fall() else chamber.fallInWindow()
            } while (isSpace)
            chamber.freeze()
            height = chamber.height(oldHeight = height)
        }

        return height + addition
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
//    val testInput = readInput("Day17_test2")
//    println(part1(testInput, 2022))
//    check(part1(testInput, 2022) == 3068)
    println(part2(testInput, 2022))
//    check(part2(testInput, 26) == 1707)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day17")
//    println(part1(input, 2022))
//    println(part2(input, 26))
}
