import java.io.File
import java.lang.Integer.parseInt

fun main() {
    val lines = File("Day14.csv").useLines { it.toList() }
    val map = ArrayList<ArrayList<Space>>()
    for(i in 0 .. 172) {
        map.add(ArrayList<Space>())
        for(j in 0 .. 1000) {
            map[i].add(Space.EMPTY)
        }
    }

    for(i in 0 .. 1000) {
        map[171][i] = Space.ROCK
    }

    for(l in lines) {
        val split = l.split(" -> ")
        var cur: Pair<Int, Int>? = null
        for(s in split) {
            val coords = s.split(",")
            val next = Pair(parseInt(coords.first()), parseInt(coords.last()))
            if(cur != null) {
                if(cur.first == next.first) {
                    // draw vertical
                    for(i in Math.min(cur.second, next.second) .. Math.max(cur.second, next.second)) {
                        map[i][cur.first] = Space.ROCK
                    }
                } else {
                    for(i in Math.min(cur.first, next.first) .. Math.max(cur.first, next.first)) {
                        map[cur.second][i] = Space.ROCK
                    }
                }
            }

            cur = next
        }
    }

    // 0, 60
    var sandCanMove = true
    var count = 0
    while(sandCanMove) {

        var curPostion = Pair(500, 0)
        val movedSand = moveSand(curPostion, map)
        if(movedSand != Pair(500, 0)) {
            println(movedSand)
            map[movedSand.second][movedSand.first] = Space.SAND
            ++count
        } else {
            sandCanMove = false
        }
//        for(i in 0 until map.size) {
//            for(j in 0 until map.first().size) {
//                print(map[i][j].char)
//            }
//            println()
//        }
    }
    println(count + 1)



    //440 521 16 169
    //500,0

    // x - 440
}

fun moveSand(pos: Pair<Int, Int>, map: ArrayList<ArrayList<Space>>): Pair<Int, Int> {
    // down
    if (pos.second + 1 < map.size &&
        map[pos.second + 1][pos.first] == Space.EMPTY) {
        return moveSand(Pair(pos.first, pos.second + 1), map)
    } else if(pos.first > 0
        && pos.second + 1 < map.size
        && map[pos.second + 1][pos.first - 1] == Space.EMPTY) {
        return moveSand(Pair(pos.first - 1, pos.second + 1), map)
    } else if(pos.first + 1 < map.first().size
        && pos.second + 1 < map.size
        && map[pos.second + 1][pos.first + 1] == Space.EMPTY) {
        return moveSand(Pair(pos.first + 1, pos.second + 1), map)
    } else if(pos.first > 0
        && pos.first + 1 < map.first().size
        && pos.second + 1 < map.size) {
        return pos
    } else {
        return Pair(-99, -99)
    }
}

enum class Space(val char: Char) {
    SAND('+'),
    ROCK('#'),
    EMPTY('.')
}