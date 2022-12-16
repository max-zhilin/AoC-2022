class PathNode(val parent: PathNode?) {
    var left: PathNode? = null
    var right: PathNode? = null
    var name: String? = null
    fun path(): String {
        return (parent?.path() ?: "") +
                when (this) {
                    parent?.left -> "L"
                    parent?.right -> "R"
                    else -> ""
                }
    }

    fun stops(): Int {
        return (parent?.stops() ?: 0) +
                if (parent?.left != null && parent?.right != null) 1 else 0
    }

    fun minStops(): Pair<PathNode, Int> {
        println(this)
        val leftStops = left?.minStops()
        val rightStops = right?.minStops()
        val thisStops = this.minStops()
        val min = minOf(
                left?.minStops(),
                right?.minStops(),
                this.minStops(),
            compareBy(nullsLast()) { it?.second })

        if (left == null && right == null) return Pair(this, stops())
        if (left != null && right != null) {
            val leftPair = left!!.minStops()
            val rightPair = right!!.minStops()
            if (leftPair.second < rightPair.second) return Pair(leftPair.first, leftPair.second + 1)
            if (leftPair.second > rightPair.second) return Pair(rightPair.first, rightPair.second + 1)
            return if (left!!.path() < right!!.path()) Pair(leftPair.first, leftPair.second + 1) else Pair(rightPair.first, rightPair.second + 1)
        }
        return if (left != null) left!!.minStops() else right!!.minStops()
    }

    override fun toString(): String {
        return path() + " : " + (name ?: "") + stops()
    }

    fun add(path: String, name: String) {
        if (path.isEmpty())
            this.name = name
        else
            when (path.first()) {
                'L' -> left = (left ?: PathNode(this)).apply { add(path.drop(1), name) }
                'R' -> right = (right ?: PathNode(this)).apply { add(path.drop(1), name) }
            }
    }
}

fun main() {
    fun part1(input: List<String>): String {
        val root = PathNode(null)
        input.forEach { line ->
            val (name, path) = line.split(" - ")
            root.add(path, name)
        }

        val result = root.minStops().first
        return result.name!!
    }

    fun part2(input: List<String>): String {
        return ""
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14meilisearch1_test")
    println(part1(testInput))
//    check(part1(testInput) == "caro")
//    println(part2(testInput))
//    check(part2(testInput) == 0)

    val input = readInput("Day14meilisearch1")
//    println(part1(input))
//    println(part2(input))
}
