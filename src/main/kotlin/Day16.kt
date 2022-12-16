import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
    val lines = File("Day16test.csv").useLines { it.toList() }

    val tunnels = HashMap<String, Tunnel>()

    for(l in lines) {
        val split = l.split(",")
        val name = split.first()
        val flowRate = split[1]

        val tunnel = Tunnel(name, hashSetOf<String>(), parseInt(flowRate), hashMapOf())
        for(i in 2 until split.size) {
            tunnel.neighbors.add(split[i])
        }

        tunnels[name] = tunnel
    }

    println(tunnels)
    for(t in tunnels) {
        calcDist(tunnels, t.value, t.value, hashSetOf(), 0)
        val newDist = t.value.dist.filter { tunnels[it.key]!!.flowRate != 0 }
        t.value.dist = newDist as HashMap<String, Int>
    }

//
//    println(tunnels)
//    var countDown = 0
//    for(i in 1..30) {
//        totalReleased += perMinute
//
//    }
    val memo = ArrayList<HashMap<HashSet<String>, HashMap<HashSet<String>, Int>>>()
    val memoOff = ArrayList<HashMap<String, Int>>()

    for(i in 0..30) {
        memo.add(hashMapOf())
        memoOff.add(hashMapOf())
    }

    println(calcMax(1, tunnels["AA"]!!, tunnels, memo, hashSetOf(""), tunnels["AA"]!!))

    println(memo[2])

}

data class Tunnel(val name: String, val neighbors: HashSet<String>, val flowRate: Int, var dist: HashMap<String, Int>)

fun calcDist(tunnels: HashMap<String, Tunnel>, start: Tunnel, tunnel: Tunnel, seen: HashSet<String>, sum: Int) {
    if (seen.contains(tunnel.name)) {
        return
    }
    seen.add(tunnel.name)

    for(t in tunnel.neighbors) {
        println(t)

        start.dist[t] = Math.min(start.dist[t] ?: Int.MAX_VALUE, sum + 1)
        calcDist(tunnels, start, tunnels.get(t)!!, seen, sum + 1)

    }
}
var count = 0
var memoCount = 0

fun calcMax(time: Int, tunnel: Tunnel, tunnels: HashMap<String, Tunnel>, memo: ArrayList<HashMap<HashSet<String>, HashMap<HashSet<String>, Int>>>, on: HashSet<String>, eTunnel: Tunnel): Int {
    if((time == 26) || on.size == 15) {
        return 0
    }


//    println(memo[24].size)

//    if(memo[time][on]?.containsKey(Pair(tunnel.name, eTunnel.name)) == true) {
//        return memo[time][on]?.get(Pair(tunnel.name, eTunnel.name))!!
//    }



//    val memoCheck = on.reduce { i, j -> i + j }

//    println(on.size)

    if(memo[time][on]?.containsKey(setOf(tunnel.name, eTunnel.name)) == true) {
//        println("memo ${++memoCount}")
        return memo[time][on]?.get(setOf(tunnel.name, eTunnel.name))!!
    }

    var canMove = false
    for(a in tunnel.dist) {
        if(a.value < 26 - time - 1 && !on.contains(a.key)) {
            canMove = true
        }
    }

    for(a in eTunnel.dist) {
        if(a.value < 26 - time - 1 && !on.contains(a.key)) {
            canMove = true
        }
    }
//
    if(canMove == false) {
        val onMoveClone = on.map { it }.toHashSet()
        memo[time].putIfAbsent(onMoveClone, hashMapOf())
        memo[time][onMoveClone]?.set(hashSetOf(tunnel.name, eTunnel.name), 0)
        return 0
    }

    count++

    if(count % 100000 == 0) {
        println(count)
    }
//    println(memo[time].size)

    val selfOnly =
        if (!on.contains(tunnel.name) && tunnel.flowRate != 0) {
            var eMax = 0
            on.add(tunnel.name)
            for (t in eTunnel.neighbors) {
                val max = calcMax(time + 1, tunnel, tunnels, memo, on, tunnels[t]!!)
                val selfOnLocal = tunnel.flowRate * (26 - time) + max
                if (selfOnLocal > eMax) {
                    eMax = selfOnLocal
                }
            }
            on.remove(tunnel.name)
            eMax
        } else {
            0
        }


    val elephantOnly =
        if (!on.contains(eTunnel.name) && eTunnel.flowRate != 0) {
            var eMax = 0
            on.add(eTunnel.name)
            for (t in tunnel.neighbors) {
                val max = calcMax(time + 1, tunnels[t]!!, tunnels, memo, on, eTunnel)
                val selfOnLocal = eTunnel.flowRate * (26 - time) + max
                if (selfOnLocal > eMax) {
                    eMax = selfOnLocal
                }
            }
            on.remove(eTunnel.name)
            eMax
        } else {
            0
        }


    val both =
        if (!on.contains(tunnel.name) && tunnel.flowRate != 0
            && !on.contains(eTunnel.name) && eTunnel.flowRate != 0 && tunnel != eTunnel
        ) {
            on.add(eTunnel.name)
            on.add(tunnel.name)
            val max = calcMax(time + 1, tunnel, tunnels, memo, on, eTunnel)
            val selfOnLocal = eTunnel.flowRate * (26 - time) + tunnel.flowRate * (26 - time) + max
            on.remove(tunnel.name)
            on.remove(eTunnel.name)
            selfOnLocal
        } else {
            0
        }

    var maxTravel = 0
    for(t in tunnel.neighbors) {
        for(et in eTunnel.neighbors) {
            val max = calcMax(time + 1, tunnels[t]!!, tunnels, memo, on, tunnels[et]!!)
            if (max > maxTravel) {
                maxTravel = max
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
    val onClone = on.map { it }.toHashSet()

    memo[time].putIfAbsent(onClone, hashMapOf())
    memo[time][onClone]?.set(hashSetOf(tunnel.name, eTunnel.name), max)

    return max
}

