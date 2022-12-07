import java.io.File

fun main() {
    val lines = File("Day3.csv").useLines { it.toList() }
    var count = 0
    for(i in 0 until lines.count() / 3) {
        println(i)
        val a1 = mutableListOf<Char>()
        val a2 = mutableListOf<Char>()
        val a3 = mutableListOf<Char>()

        for(j in 0 until lines[i*3].length) {
            a1.add(lines[i*3][j])
        }
        for(j in 0 until lines[i*3 + 1].length) {
            a2.add(lines[i*3 + 1][j])
        }
        for(j in 0 until lines[i*3 + 2].length) {
            a3.add(lines[i*3 + 2][j])
        }

        count += charToScore(a1.intersect(a2).intersect(a3).first())
    }

    println(count)
}

fun charToScore(c: Char): Int {
    if(c - 'A' in 0..25) {
        return c - 'A' + 27
    }
    return c - 'a' + 1
}
