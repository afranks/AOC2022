import java.io.File

fun main() {
    val lines = File("Day6.csv").useLines { it.toList() }
    val line = lines.first()
//    val characterCounts = HashMap<Char,Int>()

    val cnt = line.windowed(14).mapIndexed { i, l -> i to l}.first {it.second.toSet().size == 14}.first
    println(cnt + 14)
//    for(i in line.indices) {
//        val curSet = HashSet<Char>()
//        var good = true
//        for(j in 0 until 14) {x
//            if(curSet.contains(line[j + i])) {
//                good = false
//                break;
//            }
//            curSet.add(line[j + i])
//        }
//
//        if(good) {
//            println(i + 13 + 1)
//            return
//        }
//    }


}
