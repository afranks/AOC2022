import java.io.File
import java.lang.Integer.parseInt

fun main() {
    val pairs = File("Day4.csv").useLines { it.toList() }.map {
        val pairs = it.split(',')
        val first = pairs.first().split('-')
        val second = pairs.last().split('-')

        Pair(Pair(parseInt(first.first()),parseInt(first.last())), Pair(parseInt(second.first()),parseInt(second.last())))
    }
    var count = 0;
    for(p in pairs) {
        if (p.first.first >= p.second.first && p.first.second <= p.second.second ) {
            println(p)
            count++
        } else if(p.second.first >= p.first.first && p.second.second <= p.first.second) {
            println(p)
            count++
        } else if(p.second.first >= p.first.first && p.second.first <= p.first.second
            || p.first.first >= p.second.first && p.first.first <= p.second.second
            || p.first.second <= p.second.second && p.first.second >= p.second.first
            || p.second.second <= p.first.second && p.second.second >= p.first.first

            ) {
            println(p)
            count++
        }
    }
    println(count)
}

