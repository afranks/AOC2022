import java.io.File
import java.lang.Integer.parseInt

fun main() {
    val lines = File("Day7.csv").useLines { it.toList() }
    val root = Dir(hashMapOf(), hashMapOf(), null)
    var cur = root
    var curLine = 0
    while(curLine < lines.size) {
        val l = lines[curLine]
        println(cur == root)
        if(l.first() == '$') {
            // command
            val cmd = l.split(' ')
            if(cmd[1] == "cd") {
                // cd
                curLine++
                val newDirName = cmd[2]
                println("cd says $newDirName")
                if(newDirName == "/") {
                    continue;
                }

                if(cur.dirs.containsKey(newDirName)) {
                    println("changing dirs to $newDirName")
                    cur = cur.dirs[newDirName]!!
                    continue
                }

                if(newDirName == ".." && cur.parent != null) {
                    println(cur.name)
                    println("moving backwards!")
                    cur = cur.parent!!
                    println(cur.name)
                    continue
                }
            } else {
                // populate current dir
                curLine++
                while (curLine < lines.size && lines[curLine].first() != '$') {
                    val dirLine = lines[curLine]
                    println(dirLine)
                    val split = dirLine.split(" ")
                    if(split.first() == "dir") {
                        println("adding ${split[1]!!}")
                        cur.dirs.putIfAbsent(split[1]!!, Dir(parent = cur, name = split[1]!!))
                    } else {
                        println("adding file ${split[1]!!} size ${split.first()}")
                        cur.files.putIfAbsent(split[1]!!, FFile(parseInt(split.first()!!), name = split[1]!!))
                    }
                    curLine++
                }
            }
        }
    }

    println("done")

    println(root.size().first)
    println(root.size().second)

    val totalUsed = root.size().first
    val currentSpace = 70000000 - totalUsed
    val needed = 30000000 - currentSpace
    println(currentSpace)

    println(root.findTarget(needed))

}

data class Dir(val dirs: HashMap<String, Dir> = hashMapOf(), val files: HashMap<String, FFile> = hashMapOf(), val parent: Dir? = null, val name: String = "")

data class FFile(val size: Int, val name: String = "")

fun Dir.findTarget(target: Int): Int {
    var curSum = 0;
    for (f in files.values) {
        curSum += f.size
    }
    var curTar = Int.MAX_VALUE

    for (d in dirs.values) {
        val s = d.size()
        curSum += s.first

        val innerTarget = d.findTarget(target)
        if(innerTarget > target) {
            curTar = Math.min(innerTarget, curTar)
        }
    }

    if(curSum > target) {
        curTar = Math.min(curTar, curSum)
    }

    return curTar
}

fun Dir.size(): Pair<Int, Int> {
    var curSum = 0;
    var totSum = 0
    var fileSum = 0

    for (f in files.values) {
//        println(f.size)
        fileSum += f.size
    }

    curSum += fileSum
    for (d in dirs.values) {
        val s = d.size()
        curSum += s.first
        totSum += s.second
    }

    if (curSum <= 100000) {
        totSum += curSum
    }

    return Pair(curSum, totSum)
}

