import java.util.*

fun List<String>.isVisible(row: Int, col: Int): Boolean {
    val rowSize = size
    val colSize = this[0].length
    if (row == 0 || row == rowSize - 1 || col == 0 || col == colSize - 1) return true
    val tree = this[row][col]
    var visible = false
    var lineVisible = true
    for (i in 0 until row) if (this[i][col] >= tree) lineVisible = false
    visible = visible || lineVisible
    lineVisible = true
    for (i in row + 1 until rowSize) if (this[i][col] >= tree) lineVisible = false
    visible = visible || lineVisible
    lineVisible = true
    for (i in 0 until col) if (this[row][i] >= tree) lineVisible = false
    visible = visible || lineVisible
    lineVisible = true
    for (i in col + 1 until colSize) if (this[row][i] >= tree) lineVisible = false
    visible = visible || lineVisible
//    if (visible) println("$row $col")
    return visible
}
fun List<String>.countVisible(row: Int, col: Int): Int {
    val maxRow = size - 1
    val maxCol = this[0].length - 1
    if (row == 0 || row == maxRow || col == 0 || col == maxCol) return 0
    val tree = this[row][col]
    var res = 1
    var i = row - 1
    while (true) {
        if (this[i][col] >= tree || i == 0) break
        i--
    }
    res *= row - i

    i = row + 1
    while (true) {
        if (this[i][col] >= tree || i == maxRow) break
        i++
    }
    res *= row - i

    i = col - 1
    while (true) {
        if (this[row][i] >= tree || i == 0) break
        i--
    }
    res *= col - i

    i = col + 1
    while (true) {
        if (this[row][i] >= tree || i == maxCol) break
        i++
    }
    res *= col - i

    return res
}
fun main() {
    fun part1(input: List<String>): Int {

        var sum = 0
        for (row in input.indices) {
            for (col in 0 until input[0].length) {
                if (input.isVisible(row, col)) sum++
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var max = 0
        for (row in input.indices) {
            for (col in 0 until input[0].length) {
                max = maxOf(max, input.countVisible(row, col))
            }
        }

        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
//    println(part1(testInput))
    check(part1(testInput) == 21)
//    println(part2(testInput))
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
