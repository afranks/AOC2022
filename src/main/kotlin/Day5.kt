import java.io.File
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val lines = File("Day5.csv").useLines { it.toList() }
    val stacks = ArrayList<Stack<Char>>()
    for(i in 0 until 9) {
        stacks.add(Stack())
    }

    for(i in 7 downTo 0) {
        val l = lines[i];
        for(j in 0 until 9) {
            val pos = 1 + j * 4
            if(l[pos] != ' ') {
                stacks[j].push(l[pos])
            }
        }
    }

    for(i in 10 until lines.size) {
        val (count, from, to) = "[0-9]+".toRegex().findAll(lines[i]).toList().map { parseInt(it.value) }

        val fromStack = stacks[from - 1]
        val toStack = stacks[to - 1]
        val tempStack = Stack<Char>()

        for(j in 0 until count) {
            val cur = fromStack.pop()
            tempStack.push(cur)
        }

        for(j in 0 until tempStack.size) {
            toStack.push(tempStack.pop())
        }
    }

    var out = ""
    for(i in 0 until 9) {
        out += stacks[i].peek()
    }
    println(out)
}