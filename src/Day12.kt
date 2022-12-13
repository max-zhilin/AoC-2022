fun main() {
    fun part1(input: List<String>): Int {
        fun map(col: Int, row: Int): Char = input[row][col]
        fun height(col: Int, row: Int): Char {
            val c = input[row][col]
            if (c == 'S') return 'a'
            if (c == 'E') return 'z'
            return c
        }

        val (rows, cols) = listOf(input.size, input[0].length)
        val steps = Array(cols) { IntArray(rows) { -1 } }
        steps[138][20] = -2
        var exitFound = false
        var step = 0
        while (!exitFound) {
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    if (step == 0 && map(x, y) == 'S') steps[x][y] = 0
                    if (steps[x][y] != step) continue
                    exitFound = exitFound || map(x, y) == 'E'
                    fun go(toX: Int, toY: Int) {
                        if (toX < 0 || toX == cols || toY < 0 || toY == rows) return
                        if (height(toX, toY) <= height(x, y) + 1 && steps[toX][toY] < 0) steps[toX][toY] = step + 1
                    }
                    go(x, y + 1)
                    go(x, y - 1)
                    go(x + 1, y)
                    go(x - 1, y)


                }
            }
            if (!exitFound) step++
        }

//        println(monkeys)

        return step
    }

    fun part2(input: List<String>): Int {
        fun map(col: Int, row: Int): Char = input[row][col]
        fun height(col: Int, row: Int): Char {
            val c = input[row][col]
            if (c == 'S') return 'a'
            if (c == 'E') return 'z'
            return c
        }

        val (rows, cols) = listOf(input.size, input[0].length)
        var min = Int.MAX_VALUE

        for (j in 0 until rows) {
            for (i in 0 until cols) {
                if (height(i, j) == 'a') {

                    val steps = Array(cols) { IntArray(rows) { -1 } }
                    steps[i][j] = 0
                    var exitFound = false
                    var step = 0
                    while (!exitFound) {
                        for (y in 0 until rows) {
                            for (x in 0 until cols) {
                                if (step == 0 && map(x, y) == 'S') steps[x][y] = 0
                                if (steps[x][y] != step) continue
                                exitFound = exitFound || map(x, y) == 'E'
                                fun go(toX: Int, toY: Int) {
                                    if (toX < 0 || toX == cols || toY < 0 || toY == rows) return
                                    if (height(toX, toY) <= height(x, y) + 1 && steps[toX][toY] < 0) steps[toX][toY] =
                                        step + 1
                                }
                                go(x, y + 1)
                                go(x, y - 1)
                                go(x + 1, y)
                                go(x - 1, y)


                            }
                        }
                        if (!exitFound) step++
                    }

                    min = minOf(step, min)
                }
            }
        }
        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
//    println(part1(testInput))
//    check(part1(testInput) == 31)
//    println(part2(testInput))
//    check(part2(testInput) == 29)

    val input = readInput("Day12")
//    println(part1(input))
    println(part2(input))
}
