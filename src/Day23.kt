import java.util.*

//const val RIGHT = 0
//const val DOWN = 1
//const val LEFT = 2
//const val UP = 3

const val GND = '.'
const val ELF = '#'

const val N = 0
const val S = 1
const val W = 2
const val E = 3


data class Elf(var x: Int, var y: Int)

fun main() {
    fun part1(input: List<String>): Int {
        val size = 2000
        val center = size / 2
        var grove = Array(size) { CharArray(size) { '0' } }
        var nextGrove = Array(size) { CharArray(size) { '0' } }
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                grove[row + center][col + center] = if (char == GND) '0' else char
            }
        }
        var (ar, ac) = listOf(center, center)
        var (br, bc) = listOf(center + input.lastIndex, center + input[0].lastIndex)
        var direction = N
        fun rowIsFree(r: Int, c: Int): Boolean =
            grove[r][c - 1] in '0'..'4' && grove[r][c] in '0'..'4' && grove[r][c + 1] in '0'..'4'
        fun colIsFree(r: Int, c: Int): Boolean =
            grove[r - 1][c] in '0'..'4' && grove[r][c] in '0'..'4' && grove[r + 1][c] in '0'..'4'
        fun decideElf(r: Int, c: Int) {
            if (rowIsFree(r - 1, c) &&
                rowIsFree(r + 1, c) &&
                colIsFree(r, c - 1) &&
                colIsFree(r, c + 1)
                ) return // stay here
            for (i in 0..3) {
                when((i + direction) % 4) {
                    N -> if (rowIsFree(r - 1, c)) { grove[r][c] = 'N'; grove[r - 1][c]++; return }
                    S -> if (rowIsFree(r + 1, c)) { grove[r][c] = 'S'; grove[r + 1][c]++; return }
                    W -> if (colIsFree(r, c - 1)) { grove[r][c] = 'W'; grove[r][c - 1]++; return }
                    E -> if (colIsFree(r, c + 1)) { grove[r][c] = 'E'; grove[r][c + 1]++; return }
                }
            }
        }
        fun moveElf(r: Int, c: Int) {
            val (nextR, nextC) = when(grove[r][c]) {
                'N' -> Pair(r - 1, c)
                'S' -> Pair(r + 1, c)
                'W' -> Pair(r, c - 1)
                'E' -> Pair(r, c + 1)
                else -> error("can't be here")
            }
            if (grove[nextR][nextC] == '1')
                nextGrove[nextR][nextC] = ELF // move
            else
                nextGrove[r][c] = ELF // can't move
        }

        repeat(10) {
            for (r in ar..br)
                for (c in ac..bc)
                    if (grove[r][c] == ELF)
                        decideElf(r, c)

            for (r in ar..br)
                for (c in ac..bc)
                    if (grove[r][c] == ELF) // want to stay in place
                        nextGrove[r][c] = ELF
                    else if (grove[r][c] in listOf('N', 'S', 'W', 'E'))
                        moveElf(r, c)
            grove = nextGrove//.toList()
            nextGrove = Array(size) { CharArray(size) { '0' } }
            ar = grove.indexOfFirst { it.contains(ELF) }
            ac = grove.filter { it.contains(ELF) }.minOf { it.indexOf(ELF) }
            br = grove.indexOfLast { it.contains(ELF) }
            bc = grove.maxOf { it.lastIndexOf(ELF) }
            direction = (direction + 1) % 4
        }
        var sum = 0
        for (r in ar..br)
            for (c in ac..bc)
                if (grove[r][c] == '0') sum++


        return sum
    }

    fun part2(input: List<String>): Int {
        val size = 2000
        val center = size / 2
        var grove = Array(size) { CharArray(size) { '0' } }
        var nextGrove = Array(size) { CharArray(size) { '0' } }
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                grove[row + center][col + center] = if (char == GND) '0' else char
            }
        }
        var (ar, ac) = listOf(center, center)
        var (br, bc) = listOf(center + input.lastIndex, center + input[0].lastIndex)
        var direction = N
        fun rowIsFree(r: Int, c: Int): Boolean =
            grove[r][c - 1] in '0'..'4' && grove[r][c] in '0'..'4' && grove[r][c + 1] in '0'..'4'
        fun colIsFree(r: Int, c: Int): Boolean =
            grove[r - 1][c] in '0'..'4' && grove[r][c] in '0'..'4' && grove[r + 1][c] in '0'..'4'
        fun decideElf(r: Int, c: Int) {
            if (rowIsFree(r - 1, c) &&
                rowIsFree(r + 1, c) &&
                colIsFree(r, c - 1) &&
                colIsFree(r, c + 1)
            ) return // stay here
            for (i in 0..3) {
                when((i + direction) % 4) {
                    N -> if (rowIsFree(r - 1, c)) { grove[r][c] = 'N'; grove[r - 1][c]++; return }
                    S -> if (rowIsFree(r + 1, c)) { grove[r][c] = 'S'; grove[r + 1][c]++; return }
                    W -> if (colIsFree(r, c - 1)) { grove[r][c] = 'W'; grove[r][c - 1]++; return }
                    E -> if (colIsFree(r, c + 1)) { grove[r][c] = 'E'; grove[r][c + 1]++; return }
                }
            }
        }
        fun moveElf(r: Int, c: Int) {
            val (nextR, nextC) = when(grove[r][c]) {
                'N' -> Pair(r - 1, c)
                'S' -> Pair(r + 1, c)
                'W' -> Pair(r, c - 1)
                'E' -> Pair(r, c + 1)
                else -> error("can't be here")
            }
            if (grove[nextR][nextC] == '1')
                nextGrove[nextR][nextC] = ELF // move
            else
                nextGrove[r][c] = ELF // can't move
        }

        var round = 0
        do {
            round++
            for (r in ar..br)
                for (c in ac..bc)
                    if (grove[r][c] == ELF)
                        decideElf(r, c)

            for (r in ar..br)
                for (c in ac..bc)
                    if (grove[r][c] == ELF) // want to stay in place
                        nextGrove[r][c] = ELF
                    else if (grove[r][c] in listOf('N', 'S', 'W', 'E'))
                        moveElf(r, c)
            val stopped = grove.all { it.all { it in listOf('0', '#') } }
            grove = nextGrove
            nextGrove = Array(size) { CharArray(size) { '0' } }
            ar = grove.indexOfFirst { it.contains(ELF) }
            ac = grove.filter { it.contains(ELF) }.minOf { it.indexOf(ELF) }
            br = grove.indexOfLast { it.contains(ELF) }
            bc = grove.maxOf { it.lastIndexOf(ELF) }
            direction = (direction + 1) % 4
        } while (!stopped)

        return round
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
//    val testInput2 = readInput("Day23_test2")
//    println(part1(testInput))
//    println(part1(testInput2))
//    check(part1(testInput2) == 33)
//    check(part1(testInput) == 110)

//    println(part2(testInput))
    check(part2(testInput) == 20)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day23")
//    check(part1(input)) == 3947
//    check(part2(input)) ==
//    println(part1(input))
    println(part2(input))
}
