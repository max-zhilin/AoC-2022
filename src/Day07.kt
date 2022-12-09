import java.util.*

class TreeNode(val size: Int, val name: String){
    var parent: TreeNode? = null

    var children: MutableMap<String, TreeNode> = mutableMapOf()

    fun addChild(node:TreeNode){
        children[node.name] = node
        node.parent = this
    }
    fun calcSize(): Int {
        var sum = size
        if (children.isNotEmpty()) {
            sum += children.values.sumOf { it.calcSize() }
        }
        return sum
    }
    fun sumDirSize(limit: Int, list: MutableList<Int>) {
        val size = calcSize()
        if (size <= limit && this.size == 0) list.add(size)
        if (children.isNotEmpty()) {
            children.forEach { it.value.sumDirSize(limit, list) }
        }
    }
    fun minDirSize(limit: Int, list: MutableList<Int>) {
        val size = calcSize()
        if (size >= limit && this.size == 0) list.add(size)
//        list.add(size)
        if (children.isNotEmpty()) {
            children.forEach { it.value.minDirSize(limit, list) }
        }
    }
    override fun toString(): String {
        var s = "$size $name"
        if (children.isNotEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }

    fun child(dirName: String): TreeNode {
        return children[dirName]!!
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val root = TreeNode(0, "/")
        var currentNode = root

        var readDirState = false
        input.forEach() { line ->
            val scanner = Scanner(line)
            if (readDirState && line.first() != '$') {
                val typeOrSize = scanner.next()
                currentNode.addChild(
                    if (typeOrSize == "dir") TreeNode(0, scanner.next())
                        else TreeNode(typeOrSize.toInt(), scanner.next())
                )
            } else {
                readDirState = false
                when (scanner.next()) {
                    "$" -> {
                        when (scanner.next()) {
                            "cd" -> {
                                val dirName = scanner.next()
                                currentNode = when (dirName) {
                                    ".." -> currentNode.parent!!
                                    "/" -> root
                                    else -> currentNode.child(dirName)
                                }
                            }
                            "ls" -> readDirState = true
                            else -> error("wrong command $line")
                        }
                    }
                    else -> error("no $ sign in $line")
                }
            }
        }

//        println(root)

        val list = mutableListOf<Int>()
        root.sumDirSize(100_000, list)
//        println(list)
        return list.sum()
    }

    fun part2(input: List<String>): Int {
        val root = TreeNode(0, "/")
        var currentNode = root

        var readDirState = false
        input.forEach() { line ->
            val scanner = Scanner(line)
            if (readDirState && line.first() != '$') {
                val typeOrSize = scanner.next()
                currentNode.addChild(
                    if (typeOrSize == "dir") TreeNode(0, scanner.next())
                    else TreeNode(typeOrSize.toInt(), scanner.next())
                )
            } else {
                readDirState = false
                when (scanner.next()) {
                    "$" -> {
                        when (scanner.next()) {
                            "cd" -> {
                                val dirName = scanner.next()
                                currentNode = when (dirName) {
                                    ".." -> currentNode.parent!!
                                    "/" -> root
                                    else -> currentNode.child(dirName)
                                }
                            }
                            "ls" -> readDirState = true
                            else -> error("wrong command $line")
                        }
                    }
                    else -> error("no $ sign in $line")
                }
            }
        }

//        println(root)

        val list = mutableListOf<Int>()
        root.minDirSize(4_804_833, list)
//        root.minDirSize(0, list)
        println(list.sorted())
        println(70_000_000 - root.calcSize() - 30_000_000)
        return list.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
//    println(part1(testInput))
//    check(part1(testInput) == 95437)
//    println(part2(testInput))
//    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
//    println(part1(input))
    println(part2(input))
}
