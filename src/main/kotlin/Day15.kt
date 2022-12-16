import java.io.File
import java.lang.Integer.parseInt
import kotlin.IllegalArgumentException
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {
    val lines = File("Day15.csv").useLines { it.toList() }
    val inputs = ArrayList<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    val beacons = HashSet<Pair<Int, Int>>()
    for(l in lines) {
        val split = l.split(",")
        inputs.add(Pair(Pair(parseInt(split[0]),parseInt(split[1])),
            Pair(parseInt(split[2]),parseInt(split[3]))))
        beacons.add(Pair(parseInt(split[2]),parseInt(split[3])))
    }


    val min = 0
    val max = 4000000

    for(y in min .. max) {
//        println(y)
        val rowCoords = ArrayList<IntRange>()
        for(i in inputs) {
            val maxDist = calcBeaconDist(i.first, i.second)
            val left = maxDist - Math.abs(y - i.first.second)
            if(left > 0) {
                val min = Math.max(i.first.first - left, min)
                val max = Math.min(i.first.first + left, max)
                rowCoords.add(min..max)
            }
        }
        val containsTarget = containsTarget(rowCoords.sortedBy { it.first  })
        if(containsTarget != -1) {
            println(containsTarget.toBigInteger() * 4000000.toBigInteger() + y.toBigInteger())
            break
        }

    }
    println("done")
}

fun containsTarget(a: List<IntRange>): Int {
    var current = a.first()

    for(i in 1 until a.size) {
//        println(current)

        val next = a[i]
        if(next.first <= current.last) {
            current = current.merge(next)
        } else {
            return current.last + 1
        }
    }
    return -1
}

fun calcBeaconDist(sensor: Pair<Int, Int>, beacon: Pair<Int, Int>): Int {
    return Math.abs(sensor.first - beacon.first) + Math.abs(sensor.second - beacon.second)
}

fun IntRange.merge(other: IntRange): IntRange {
    if(contains(other.first) || contains(other.last)) {
        return first.coerceAtMost(other.first)..last.coerceAtLeast(other.last)
    }

    throw IllegalArgumentException()
}