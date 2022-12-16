import java.io.File
import java.lang.Integer.parseInt

fun main() {
    val lines = File("Day10.csv").useLines { it.toList() }
    val instructions = ArrayList<Instr>()
    val crt = ArrayList<Boolean>()

    for(l in lines) {
        val split = l.split(" ")
        val instr = when(split.first()) {
            "noop" -> Instr(Op.NOOP)
            "addx" -> Instr(Op.ADDX, parseInt(split.last()))
            else -> Instr(Op.NOOP)
        }
        instructions.add(instr)
    }

    var cycleCounter = 1
    var reg = 1
    crt.add(true)
    for(instr in instructions) {
        when(instr.instr) {
            Op.NOOP -> {
                cycleCounter++
                crt.add((cycleCounter - 1) % 40  >= reg - 1 && (cycleCounter - 1)  % 40 <= reg + 1)
            }
            Op.ADDX -> {
                cycleCounter += 1
                crt.add((cycleCounter - 1) % 40  >= reg - 1 && (cycleCounter - 1)  % 40 <= reg + 1)
                reg += instr.param!!
                cycleCounter += 1
                crt.add((cycleCounter - 1) % 40  >= reg - 1 && (cycleCounter - 1)  % 40 <= reg + 1)
            }
        }
    }
    println(crt.size)
    println(crt)
    for(i in 0 until crt.size) {
        if(i % 40 == 0) {
            println()
        }
        if(crt[i]) {
            print("#")
        } else {
            print(".")
        }
    }
}

enum class Op(val time: Int?) {
    NOOP(1),
    ADDX(2)
}

data class Instr(val instr: Op, val param: Int? = 0)