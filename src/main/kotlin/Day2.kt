import java.io.File
import java.util.PriorityQueue

fun main() {
    val lines = File("Day2.csv").useLines { it.toList() }
    var score = 0

    for(l in lines) {
        val them = l[0] - 'A'
        var me = 0

        if (l[2] == 'X') {
            if(them == 0) {
                me = 2
            } else {
                me = them - 1
            }
        } else if(l[2] == 'Y') {
            me = them
        } else {
            if(them == 2) {
                me = 0
            } else {
                me = them + 1
            }
        }

        if (them == me ) {
            score += 3 + me + 1
            println("draw")
        } else if (me - them == 1 || me - them == -2) {
            score += 6 + me + 1
            println("win")
        } else if ( them - me == 1 || them - me == -2 ) {
            score += me + 1
            println("lose")
        }
    }
    println(score)
}