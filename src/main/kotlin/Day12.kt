import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    val lines = File("Day12.csv").useLines { it.toList() }

    val grid = ArrayList<ArrayList<Char>>()
    var start = Pair(0, 0)
    var end = Pair(0, 0)
    for(l in lines) {
        val cur = ArrayList<Char>()
        grid.add(cur)
        for(c in l) {
            cur.add(c)
        }
    }

    for(i in 0 until grid.size) {
        for (j in 0 until grid.first().size) {
            if(grid[i][j] == 'S') {
                start = Pair(j, i)
                grid[i][j] = 'a'
            } else if(grid[i][j] == 'E') {
                end = Pair(j, i)
                grid[i][j] = 'z'
            }
        }
    }
    start = end

    val pq = PriorityQueue<Pair<Pair<Int, Int>, Int>>( compareBy { it.second })
    val visited = HashSet<Pair<Int, Int>>()

    pq.add(Pair(start, 0))

    val around = listOf(Pair(0,1), Pair(1,0), Pair(0, -1), Pair(-1, 0))

    while(pq.isNotEmpty()) {
        val cur = pq.poll()
        if(visited.contains(cur.first)) {
            continue;
        }
        visited.add(cur.first)
        val coords = cur.first

        val curCount = cur.second
        val curHeight = grid[coords.second][coords.first] - 'a'

        if (curHeight == 0) {
            println("found end")
            println(curCount)
            println(cur.first)
            break
        }

        // try around
        for(a in around) {
            val newX = coords.first
            val newY = coords.second
            val newCoords = Pair(a.first + newX, a.second + newY)
            if(newCoords.isValid(grid)) {
                val newHeight = grid[newCoords.second][newCoords.first] - 'a'
                if(curHeight - newHeight <= 1 && !visited.contains(newCoords)) {
                    pq.add(Pair(newCoords, curCount + 1))
                }
            }
        }
    }
}

fun Pair<Int, Int>.isValid(grid: ArrayList<ArrayList<Char>>): Boolean {
    return first >= 0 && second >= 0 && first < grid.first().size && second < grid.size
}





