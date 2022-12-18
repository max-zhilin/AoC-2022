import java.util.*

fun parse(s: String): Triple<Int, Int, Int> {
    val scanner = Scanner(s)
    scanner.useDelimiter(",")
    return Triple(scanner.nextInt(), scanner.nextInt(), scanner.nextInt())
}

fun main() {
    fun part1(input: List<String>): Int {
        val (maxX, maxY, maxZ) = listOf(19, 19, 19)
//        val exist = Array(maxX + 1) { Array(maxY + 1) { BooleanArray(maxZ + 1) { false } } }
        val surface = Array(maxX + 1) { Array(maxY + 1) { IntArray(maxZ + 1) { -1 } } }
        input.forEach { line ->
            val (x, y, z) = parse(line)
            if (surface[x][y][z] != -1) error("duplicate coords")
            var self = 6
            if (x > 0    && surface[x-1][y][z] > 0) { self--; surface[x-1][y][z]-- }
            if (x < maxX && surface[x+1][y][z] > 0) { self--; surface[x+1][y][z]-- }
            if (y > 0    && surface[x][y-1][z] > 0) { self--; surface[x][y-1][z]-- }
            if (y < maxY && surface[x][y+1][z] > 0) { self--; surface[x][y+1][z]-- }
            if (z > 0    && surface[x][y][z-1] > 0) { self--; surface[x][y][z-1]-- }
            if (z < maxZ && surface[x][y][z+1] > 0) { self--; surface[x][y][z+1]-- }
            surface[x][y][z] = self
        }

        return surface.sumOf { it.sumOf { it.filter { it > 0 }.sum() } }
    }

    fun part2(input: List<String>): Int {
        val (maxX, maxY, maxZ) = listOf(19, 19, 19)
        val lavaSurface = Array(maxX + 1) { Array(maxY + 1) { IntArray(maxZ + 1) { -1 } } }

        fun calcSurface(a: Array<Array<IntArray>>, x: Int, y: Int, z: Int) {
            if (a[x][y][z] != -1) error("duplicate coords")
            var self = 6
            if (x > 0    && a[x-1][y][z] > 0) { self--; a[x-1][y][z]-- }
            if (x < maxX && a[x+1][y][z] > 0) { self--; a[x+1][y][z]-- }
            if (y > 0    && a[x][y-1][z] > 0) { self--; a[x][y-1][z]-- }
            if (y < maxY && a[x][y+1][z] > 0) { self--; a[x][y+1][z]-- }
            if (z > 0    && a[x][y][z-1] > 0) { self--; a[x][y][z-1]-- }
            if (z < maxZ && a[x][y][z+1] > 0) { self--; a[x][y][z+1]-- }
            a[x][y][z] = self
        }
        input.forEach { line ->
            val (x, y, z) = parse(line)
            calcSurface(lavaSurface, x, y, z)
        }

        val trap = Array(maxX + 1) { Array(maxY + 1) { IntArray(maxZ + 1) { -1 } } }
        val reachable = Array(maxX + 1) { Array(maxY + 1) { BooleanArray(maxZ + 1) { false } } }
        fun markReachable(x: Int, y: Int, z: Int) {
            if (x == 0 || x == maxX) return
            if (y == 0 || y == maxY) return
            if (z == 0 || z == maxZ) return

            if (reachable[x][y][z]) return

            if (lavaSurface[x][y][z] > -1) return

            reachable[x][y][z] = true

            markReachable(x - 1, y, z)
            markReachable(x + 1, y, z)
            markReachable(x, y - 1, z)
            markReachable(x, y + 1, z)
            markReachable(x, y, z - 1)
            markReachable(x, y, z + 1)
            
            return
        }
        for (y in 1 until maxY)
            for (z in 1 until maxZ) {
                if (lavaSurface[0][y][z] == -1) markReachable(1, y, z)
                if (lavaSurface[maxX][y][z] == -1) markReachable(maxX - 1, y, z)
            }
        for (x in 1 until maxX)
            for (z in 1 until maxZ) {
                if (lavaSurface[x][0][z] == -1) markReachable(x, 1, z)
                if (lavaSurface[x][maxY][z] == -1) markReachable(x, maxY - 1, z)
            }
        for (x in 1 until maxX)
            for (y in 1 until maxY) {
                if (lavaSurface[x][y][0] == -1) markReachable(x, y, 1)
                if (lavaSurface[x][y][maxZ] == -1) markReachable(x, y, maxZ - 1)
            }

        for (x in 1 until maxX)
            for (y in 1 until maxY)
                for (z in 1 until maxZ)
                    if (lavaSurface[x][y][z] == -1 && !reachable[x][y][z])
                        calcSurface(trap, x, y, z)

        return lavaSurface.sumOf { it.sumOf { it.filter { it > 0 }.sum() } }.also { println("lava $it") } -
                trap.sumOf { it.sumOf { it.filter { it > 0 }.sum() } }.also { println("trap $it") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
//    val testInput2 = readInput("Day18_test2")
//    println(part1(testInput))
//    check(part1(testInput2, 2022) == 10)
//    check(part1(testInput) == 64)
    println(part2(testInput))
    check(part2(testInput) == 58)

    @Suppress("UNUSED_VARIABLE")
    val input = readInput("Day18")
//    println(part1(input))
    println(part2(input))
}
