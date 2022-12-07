import java.io.File
import java.lang.Integer.parseInt
import java.util.PriorityQueue

fun main() {
    val lines = File("Day1.csv").useLines { it.toList() }
    var count = 0;
    var pq = PriorityQueue<Int>(10, Comparator.reverseOrder())

    for(l in lines) {
        if (l == "") {
            pq.offer(count)
            count = 0
        } else {
            count += parseInt(l)
        }
    }

    println(pq.poll() + pq.poll() + pq.poll())
}