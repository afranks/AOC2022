import java.io.File
import java.math.BigInteger

fun main() {
//    val lines = File("Day11.csv").useLines { it.toList() }

    val monkeys = ArrayList<Monkey>()

    monkeys.add(
        Monkey(mutableListOf<Int>(97, 81, 57, 57, 91, 61), {i: Int-> (i * 7).toLong() }, { i: Int-> if (i % 11 == 0) { 5 } else { 6 }}, div = 11)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(88, 62, 68, 90), {i: Int-> (i * 17).toLong() }, { i: Int-> if (i % 19 == 0) { 4 } else { 2 }}, div = 19)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(74, 87), {i: Int-> (i + 2).toLong() }, { i: Int-> if (i % 5 == 0) { 7 } else { 4 }}, div = 5)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(53, 81, 60, 87, 90, 99, 75), {i: Int-> (i + 1).toLong() }, { i: Int-> if (i % 2 == 0) { 2 } else { 1 }}, div = 2)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(57), {i: Int-> (i + 6).toLong() }, { i: Int-> if (i % 13 == 0) { 7 } else { 0 }}, div =13)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(54, 84, 91, 55, 59, 72, 75, 70), {i: Int-> (i.toBigInteger() * i.toBigInteger() % 9699690.toBigInteger()).toLong() }, { i: Int-> if (i % 7 == 0) { 6 } else { 3 }}, div = 7)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(95, 79, 79, 68, 78), {i: Int-> (i + 3).toLong() }, { i: Int-> if (i % 3 == 0) { 1 } else { 3 }}, div = 3)
    )
    monkeys.add(
        Monkey(mutableListOf<Int>(61, 97, 67), {i: Int-> (i + 4).toLong() }, { i: Int-> if (i % 17 == 0) { 0 } else { 5 }}, div = 17)
    )

    val mod = monkeys.fold(1) { acc, m -> m.div * acc}

    for(i in 0 until 10000) {
        for(m in monkeys) {
            for(item in m.items) {
                m.count++
                val newWorry = (m.worryIncrease(item) % mod).toInt()
                val nextMonkey = m.test(newWorry)
                monkeys[nextMonkey].items.add(newWorry)
            }
            m.items.clear()
        }
    }

    println(monkeys)
    val sorted = monkeys.sortedByDescending { it.count }
    println(sorted.first().count)
    println(sorted[1].count)
    println(sorted[2].count)
}

class Monkey(val items: MutableList<Int>, val worryIncrease: (Int) -> Long, val test: (Int) -> Int, val div: Int, var count: Int= 0)