class Stack{
    val elements: MutableList<Char> = mutableListOf()

    fun isEmpty() = elements.isEmpty()

    fun size() = elements.size

    fun push(item: Char) = elements.add(item)

    fun insert(item: Char) = elements.add(0, item)

    fun pop() : Char {
        val item = elements.last()
        if (!isEmpty()){
            elements.removeAt(elements.size -1)
        }
        return item
    }
    fun peek() : Char = elements.last()

    override fun toString(): String = elements.toString()
}
fun main() {
    fun part1(input: List<String>): String {
        val head = mutableListOf<String>()
        val tail = mutableListOf<String>()

        var numberFound = false
        input.forEach {
            if (!numberFound) {
                if (it.substring(0, 2) == " 1") {
                    numberFound = true
                } else {
                    head.add(it)
                }
            } else if (!it.isBlank()) {
                tail.add(it)
            }
        }

        val stacks = mutableListOf<Stack>()
        head.forEach { line ->
            line.forEachIndexed { index, c ->
                // 1 5 9
                if ((index) % 4 == 1) {
                    if (index / 4 > stacks.lastIndex) stacks.add(Stack())
                    if (c.isLetter()) stacks[index / 4].insert(c)
                }
            }
        }

//        stacks.forEach { println(it) }
//        println(tail)

        tail.forEach {
            val (quantity, _, from, _, to) = it.removePrefix("move ").split(" ").map { it.toIntOrNull() ?: 0 }
            repeat(quantity) {
                stacks[to - 1].push(stacks[from - 1].pop())
            }
        }
//        stacks.forEach { println(it) }

        var sum = ""
        stacks.forEach {
            val c = it.peek()
            if (c.isLetter()) sum += c
        }
        return sum
    }

    fun part2(input: List<String>): String {
        val head = mutableListOf<String>()
        val tail = mutableListOf<String>()

        var numberFound = false
        input.forEach {
            if (!numberFound) {
                if (it.substring(0, 2) == " 1") {
                    numberFound = true
                } else {
                    head.add(it)
                }
            } else if (!it.isBlank()) {
                tail.add(it)
            }
        }

        val stacks = mutableListOf<Stack>()
        head.forEach { line ->
            line.forEachIndexed { index, c ->
                // 1 5 9
                if ((index) % 4 == 1) {
                    if (index / 4 > stacks.lastIndex) stacks.add(Stack())
                    if (c.isLetter()) stacks[index / 4].insert(c)
                }
            }
        }

//        stacks.forEach { println(it) }
//        println(tail)

        tail.forEach {
            val (quantity, _, from, _, to) = it.removePrefix("move ").split(" ").map { it.toIntOrNull() ?: 0 }
            val temp = Stack()
            repeat(quantity) {
                temp.push(stacks[from - 1].pop())
            }
            repeat(quantity) {
                stacks[to - 1].push(temp.pop())
            }
        }
//        stacks.forEach { println(it) }

        var sum = ""
        stacks.forEach {
            val c = it.peek()
            if (c.isLetter()) sum += c
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
//    println(part1(testInput))
    check(part1(testInput) == "CMZ")
//    println(part2(testInput))
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
