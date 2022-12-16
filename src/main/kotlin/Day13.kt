import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val lines = File("Day13.csv").useLines { it.toList() }
    val elements = ArrayList<A>()

    for(i in lines.indices) {
        if((i + 1) % 3 == 0) {
            continue
        }
        var stack = Stack<A>()
        var start = A()
        elements.add(start)
        stack.add(start)
        var j = 1
        while(j < lines[i].length) {
            val char = lines[i][j]
            j++
            if (char == '[') {
                val newArray = A()
                stack.peek().array.add(newArray)
                stack.add(newArray)
            } else if(char == ']') {
                stack.pop()
            } else if(char == ',') {
                continue
            } else {
                if(lines[i][j].isDigit()) {
                    stack.peek().array.add(Num(10))
                    j++
                } else {
                    stack.peek().array.add(Num(char - '0'))
                }
            }

        }
    }
//    for(i in 0 until elements.size / 2) {
//        val first = elements[i*2]
//        val second = elements[i*2 + 1]
//        val compare = first.compare(second)
////       println("checking ${i + 1}")
//        if(compare == -1) {
//            sum += i + 1
//        }
//    }

    elements.sortWith { first, second -> first.compareArray(second) }

    println(elements.size)
    for(i in 0 until elements.size) {
        if(elements[i].array.size != 1) {
            continue
        }


        val a = elements[i].array[0]
        if(elements[i].array.size == 1 && a is A  && a.array.size == 1 && a.array[0] is Num) {
            val num = a.array[0] as Num
            if(num.num == 2 || num.num == 6) {
                println(i + 1)
            }
        }
    }

}

sealed class ListElement

class Num(val num: Int): ListElement()

class A(val array: ArrayList<ListElement> = ArrayList<ListElement>()): ListElement()

fun ListElement.compare(other: ListElement): Int {
    if(this is Num && other is Num) {
        if (this.num == other.num) {
            return 0
        } else if(this.num > other.num) {
            return 1
        } else {
            return -1
        }
    } else if(this is A && other is A) {
        return this.compareArray(other)
    } else {
        if(this is Num && other is A) {
            return A(arrayListOf(this)).compareArray(other)
        } else if(other is Num && this is A) {
            return this.compareArray(A(arrayListOf(other)))
        }
    }

    return -1
}

fun A.compareArray(other: A): Int {
    for (i in 0 until this.array.size) {
        if (i >= other.array.size) {
            return 1
        }

        val compare = this.array[i].compare(other.array[i])
        if(compare == 0) {
            continue;
        } else {
            return compare
        }
    }
    if(this.array.size < other.array.size) {
        return -1
    }
    return 0
}