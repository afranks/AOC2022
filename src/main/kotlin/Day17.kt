import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    println(drawRocks())
}
fun drawRocks() = measureTimeMillis {
    val lines = File("Day17.csv").useLines { it.toList() }
    val dirs = ArrayList<RockDir>()
    for(c in lines.first()) {
        val newDir = when(c) {
            '>' -> RockDir.RIGHT
            '<' -> RockDir.LEFT
            else -> RockDir.LEFT
        }
        dirs.add(newDir)
    }

    val grid = ArrayList<ArrayList<Boolean>>()
    for(i in 0 until 3) {
        grid.add(arrayListOf(false, false, false, false, false, false, false))
    }
    var top = -1
    var counter = 0

    val history = HashMap<Triple<ArrayList<Boolean>, RockShape, Int>, HashSet<Pair<Int, Int>>>()
    val historyTop = ArrayList<Int>()

    for(i in 0 until 100_000_000) {
        val shape = ROCK_ORDER[i % 5]
        val llY = top + 4
        val rock = Rock(Pair(llY, 2), shape)
        while(grid.size <= rock.top()) {
            grid.add(arrayListOf(false, false, false, false, false, false, false))
        }

        var vertical = false
        while(!vertical || rock.tryDown(grid)) {
            if(vertical) {
                // we can move down
                rock.moveDown(grid)
            } else {
                val dir = dirs[counter % dirs.size]
                rock.moveDir(grid, dir)
                counter++
            }
            vertical = !vertical
        }

        rock.drawRock(grid)
        top = maxOf(rock.top(), top)
        historyTop.add(top)
        if(history.contains(Triple(grid[top], shape, counter % dirs.size))) {
            val hist = history[Triple(grid[top], shape, counter % dirs.size)]?.first()
            if(hist != null) {
                val intMult = 1000000000000/(i + 1 - hist.first)
                val math = (top + 1 - hist.second)
                val addI = (1000000000000 - intMult * (i + 1 - hist.first) - hist.first).toInt()
                val valAt = historyTop[addI + hist.first-1] - historyTop[hist.first-1]
                println(i)
                println(intMult * math + hist.second + valAt)
                break
            }
        }
        history.putIfAbsent(Triple(grid[top], shape, counter % dirs.size), hashSetOf())
        history[Triple(grid[top], shape, counter % dirs.size)]?.add(Pair(i + 1, top + 1))
    }
}

fun ArrayList<ArrayList<Boolean>>.draw() {
    for(i in size - 1 downTo 0) {
        for(j in 0 until 7) {
            if(this[i][j]) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}

val ROCK_ORDER = arrayListOf<RockShape>(RockShape.FLAT, RockShape.PLUS, RockShape.EL, RockShape.VERTICAL, RockShape.SQUARE)

data class Rock(var ll: Pair<Int, Int>, val shape: RockShape) {
    fun allCoords(pos: Pair<Int, Int> = ll): ArrayList<Pair<Int, Int>> {
        val coords = ArrayList<Pair<Int, Int>>()
        for(p in shape.points) {
            val newCoord = Pair(pos.first + p.first, pos.second + p.second)
            coords.add(newCoord)
        }
        return coords
    }

    fun tryDown(grid: ArrayList<ArrayList<Boolean>>): Boolean {
        val coords = allCoords(Pair(ll.first - 1, ll.second))
        if(coords.any {it.first < 0 || grid[it.first][it.second]}) {
            return false
        }

        return true
    }

    fun moveDown(grid: ArrayList<ArrayList<Boolean>>) {
        if(tryDown(grid)) {
            ll = Pair(ll.first - 1, ll.second)
        }
    }

    fun tryDir(grid: ArrayList<ArrayList<Boolean>>, dir: RockDir): Boolean {
        val coords = allCoords(Pair(ll.first, ll.second + dir.dir))
        if(coords.any {it.second > 6 || it.second < 0 || grid[it.first][it.second]}) {
            return false
        }

        return true
    }

    fun moveDir(grid: ArrayList<ArrayList<Boolean>>, dir: RockDir) {
        if(tryDir(grid, dir)) {
            ll = Pair(ll.first, ll.second + dir.dir)
        }
    }

    fun drawRock(grid: ArrayList<ArrayList<Boolean>>) {
        while(top() >= grid.size) {
            grid.add(arrayListOf(false, false, false, false, false, false, false))
        }

        val coords = allCoords()
        for(c in coords) {
            grid[c.first][c.second] = true
        }
    }

    fun top() = allCoords().maxOf { it.first }
}

enum class RockShape(val points: ArrayList<Pair<Int, Int>>) {
    FLAT(arrayListOf(0 to 0, 0 to 1, 0 to 2, 0 to 3)),
    PLUS(arrayListOf(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2)),
    EL(arrayListOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2)),
    VERTICAL(arrayListOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)),
    SQUARE(arrayListOf(0 to 0, 1 to 0, 0 to 1, 1 to 1))
}

enum class RockDir(val dir: Int) {
    LEFT(-1),
    RIGHT(1)
}