import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
    val lines = File("Day16.csv").useLines { it.toList() }

    val tunnels = HashMap<String, Tunnel>()

    for (l in lines) {
        val split = l.split(",")
        val name = split.first()
        val flowRate = split[1]

        val tunnel = Tunnel(name, hashSetOf<String>(), parseInt(flowRate), TreeMap())
        for (i in 2 until split.size) {
            tunnel.neighbors.add(split[i])
        }

        tunnels[name] = tunnel
    }

    println(tunnels)
    for (t in tunnels) {
        calcDist(tunnels, t.value, t.value, hashSetOf(), 0)
//        val newDist = t.value.dist.filter { tunnels[it.key]!!.flowRate != 0 && it.key != t.key}
//        t.value.dist = newDist as TreeMap<String, Int>
    }

//
//    println(tunnels)
//    var countDown = 0
//    for(i in 1..30) {
//        totalReleased += perMinute
//
//    }
    val memo = ArrayList<ArrayList<HashMap<HashSet<String>, HashMap<Pair<String, String>, Int>>>>()

    for (i in 0..30) {
        val new = ArrayList<HashMap<HashSet<String>, HashMap<Pair<String, String>, Int>>>()
        memo.add(new)
        for (j in 0..30) {
            new.add(hashMapOf())
        }
    }

    println(calcMax(1, 1, tunnels["AA"]!!, tunnels, memo, hashSetOf(""), tunnels["AA"]!!))

//    println(memo[2])
    var count = 0
    for (m in memo) {
        for (t in m) {
            for (t2 in t) {
                for (h in t2.value) {
                    count++
                }
            }
        }
    }
    println(count)
}

data class Tunnel(
    val name: String,
    val neighbors: HashSet<String>,
    val flowRate: Int,
    var dist: SortedMap<Int, HashSet<String>>
)

fun calcDist(tunnels: HashMap<String, Tunnel>, start: Tunnel, tunnel: Tunnel, seen: HashSet<String>, sum: Int) {
    if (start.name == "BB") {
        println()
    }
    var q = LinkedList<Tunnel>()
    var q2 = LinkedList<Tunnel>()

    q.addLast(start)
    var count = 0

    while (q.isNotEmpty()) {
        val top = q.pop()
        if (seen.contains(top.name)) {
            continue
        }
        seen.add(top.name)
        if (top.flowRate != 0 && top.name != start.name) {
            start.dist.putIfAbsent(count, HashSet())
            start.dist[count]!!.add(top.name)
        }

        for (t in top.neighbors) {
            if (!seen.contains(t)) {
                q2.addLast(tunnels[t]!!)
            }
        }

        if (q.isEmpty() && q2.isNotEmpty()) {
            count++
            val temp = q
            q = q2
            q2 = temp
        }
    }

//    for(t in tunnel.neighbors) {
//        println(t)
//
//        start.dist[t] = Math.min(start.dist[t] ?: Int.MAX_VALUE, sum + 1)
//        calcDist(tunnels, start, tunnels.get(t)!!, seen, sum + 1)
//    }
}

var count = 0
var memoCount = 0
val maxTime = 26

fun calcMax(
    time: Int,
    eTime: Int,
    tunnel: Tunnel,
    tunnels: HashMap<String, Tunnel>,
    memo: ArrayList<ArrayList<HashMap<HashSet<String>, HashMap<Pair<String, String>, Int>>>>,
    on: HashSet<String>,
    eTunnel: Tunnel
): Int {
    if ((time >= maxTime && eTime >= maxTime)) {
        return 0
    }
//    println(memo[24].size)

//    if(memo[time][on]?.containsKey(Pair(tunnel.name, eTunnel.name)) == true) {
//        return memo[time][on]?.get(Pair(tunnel.name, eTunnel.name))!!
//    }

//    val memoCheck = on.reduce { i, j -> i + j }

//    println(on.size)

//    if(time == maxTime && eTime == 25 && on == hashSetOf("", "DD") && tunnel == tunnels["DD"] && eTunnel == tunnels["BB"]) {
//        println("here")
//    }
//    println(eTunnel.name)
    if (memo[time][eTime][on]?.containsKey(Pair(tunnel.name, eTunnel.name)) == true) {
//        println("memo hit")
//        println("memo ${++memoCount}")
        ++memoCount
        if (memoCount % 10000000 == 0) {
            println("T: $time ET: $eTime on: $on ${setOf(tunnel.name, eTunnel.name)}")
            println(memoCount)
        }
        return memo[time][eTime][on]?.get(Pair(tunnel.name, eTunnel.name))!!
    }
//    else {
//        val max = memo[time][eTime].filter { it.key.containsAll(on) }.mapNotNull {it.value.get(Pair(tunnel.name, eTunnel.name))}
//        if(max.isNotEmpty()) {
//            return max.max()
//        }
//    }

//    var canMove = false
//    for(a in tunnel.dist) {
//        if(a.value < maxTime - time - 1 && !on.contains(a.key)) {
//            canMove = true
//        }
//    }
//
//    for(a in eTunnel.dist) {
//        if(a.value < maxTime - time - 1 && !on.contains(a.key)) {
//            canMove = true
//        }
//    }
////
//    if(canMove == false) {
//        val onMoveClone = on.map { it }.toHashSet()
//        memo[time].putIfAbsent(onMoveClone, hashMapOf())
//        memo[time][onMoveClone]?.set(hashSetOf(tunnel.name, eTunnel.name), 0)
//        return 0
//    }


//    println(memo[time].size)

//    if(time == 9 && tunnel.name == "JJ" && on.containsAll(listOf("DD", "BB"))) {
//        println()
//    }

    var anyMove = false
    val selfOnly =
        if (tunnel.flowRate != 0 && time < maxTime && !on.contains(tunnel.name)) {
            var eMax = 0
            on.add(tunnel.name)
            val toAdd = tunnel.flowRate * (maxTime - time)
            var didMove = false
            for (t in eTunnel.dist.headMap(maxTime - eTime - 1)) {
                for (n in t.value) {
                    if (!on.contains(n)) {
                        didMove = true
                        val max = calcMax(time + 1, eTime + t.key, tunnel, tunnels, memo, on, tunnels[n]!!)
                        val selfOnLocal = toAdd + max
                        if (selfOnLocal > eMax) {
                            eMax = selfOnLocal
                        }
                    }
                }
            }

            if (!didMove) {
                val max = calcMax(time + 1, eTime, tunnel, tunnels, memo, on, eTunnel)
                val selfOnLocal = toAdd + max
                if (selfOnLocal > eMax) {
                    eMax = selfOnLocal
                }
                didMove = true
            }
            on.remove(tunnel.name)
            anyMove = anyMove || didMove
            eMax
        } else {
            0
        }


    val elephantOnly =
        if (eTunnel.flowRate != 0 && eTime < maxTime && !on.contains(eTunnel.name)) {
            var eMax = 0
            on.add(eTunnel.name)
            val toAdd = eTunnel.flowRate * (maxTime - eTime)
            var didMove = false

            for (t in tunnel.dist.headMap(maxTime - time - 1)) {
                for (n in t.value) {
                    if (!on.contains(n)) {
                        didMove = true
                        val max = calcMax(time + t.key, eTime + 1, tunnels[n]!!, tunnels, memo, on, eTunnel)
                        val selfOnLocal = toAdd + max
                        if (selfOnLocal > eMax) {
                            eMax = selfOnLocal
                        }
                    }
                }
            }

            if (!didMove) {
                val max = calcMax(time, eTime + 1, tunnel, tunnels, memo, on, eTunnel)
                val selfOnLocal = toAdd + max
                if (selfOnLocal > eMax) {
                    eMax = selfOnLocal
                }
                didMove = true
            }
            on.remove(eTunnel.name)
            anyMove = anyMove || didMove
            eMax
        } else {
            0
        }


    val both =
        if (tunnel != eTunnel && eTime < maxTime && time < maxTime && !on.contains(eTunnel.name) && !on.contains(tunnel.name)) {
            on.add(eTunnel.name)
            on.add(tunnel.name)
            val max = calcMax(time + 1, eTime + 1, tunnel, tunnels, memo, on, eTunnel)
            val selfOnLocal = eTunnel.flowRate * (maxTime - eTime) + tunnel.flowRate * (maxTime - time) + max
            on.remove(tunnel.name)
            on.remove(eTunnel.name)
            anyMove = true
            selfOnLocal
        } else {
            0
        }

    var maxTravel = 0
    var didMove = false
    if (!anyMove) {
        for (t in tunnel.dist.headMap(maxTime - time - 1)) {
            for (n in t.value) {
                if (!on.contains(n)) {
                    for (et in eTunnel.dist.headMap(maxTime - eTime - 1)) {
                        for (n2 in et.value) {
                            if (!on.contains(n2)) {
                                didMove = true
                                val max = calcMax(
                                    time + t.key,
                                    eTime + et.key,
                                    tunnels[n]!!,
                                    tunnels,
                                    memo,
                                    on,
                                    tunnels[n2]!!
                                )
                                if (max > maxTravel) {
                                    maxTravel = max
                                }
                            }
                        }
                        if (!didMove) {
                            val max = calcMax(time + t.key, eTime, tunnels[n]!!, tunnels, memo, on, eTunnel)
                            if (max > maxTravel) {
                                maxTravel = max
                            }
                            didMove = true
                        }
                    }
                }
            }
        }

        if (!didMove) {
            for (et in eTunnel.dist.headMap(maxTime - eTime - 1)) {
                for (n in et.value) {
                    if (!on.contains(n)) {
                        val max = calcMax(time, eTime + et.key, tunnel, tunnels, memo, on, tunnels[n]!!)
                        if (max > maxTravel) {
                            maxTravel = max
                        }
                    }
                }
            }
        }

    }
    val max = maxOf(maxTravel, selfOnly, elephantOnly, both)

//    var string = ""
//    for(s in on) {
//        string += s
//        memo[time].putIfAbsent(string, hashMapOf())
//        memo[time][string]?.set(
//           hashSetOf(tunnel.name, eTunnel.name),
//            maxOf(max, memo[time][string]?.get(hashSetOf(tunnel.name, eTunnel.name)) ?: 0 ))
//    }

    count++
//    println("$time $eTime")
    if (count % 1000000 == 0) {
        println("$time $eTime")
        println(count)
    }
    val onClone = on.map { it }.toHashSet()
//    if(time == maxTime && eTime == 25 && on == hashSetOf("", "DD") && tunnel == tunnels["DD"] && eTunnel == tunnels["BB"]) {
//        println("here")
//    }

    memo[time][eTime].putIfAbsent(onClone, hashMapOf())
    memo[time][eTime][onClone]?.set(Pair(tunnel.name, eTunnel.name), max)

    return max
}


