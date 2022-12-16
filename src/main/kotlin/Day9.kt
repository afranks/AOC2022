import java.io.File
import java.lang.Integer.parseInt

fun main() {
    val lines = File("Day9.csv").useLines { it.toList() }
    val instructions = ArrayList<Instruction>()

    for (l in lines) {
        val split = l.split(" ")
        var direction = when (split.first()) {
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> Direction.UP
        }
        val distance = parseInt(split.last())
        instructions.add(Instruction(direction, distance))
    }

    val positions = ArrayList<Pair<Int, Int>>()
    for(i in 0 until 10) {
        positions.add(Pair(0, 0))
    }
    val visitedSet = mutableSetOf(positions.first())

    for (instruction in instructions) {
        calcNewTail(instruction, visitedSet, positions)
    }

    println(visitedSet.size)
}

fun calcNewTail(
    instruction: Instruction,
    visitedSet: MutableSet<Pair<Int, Int>>,
    positions: ArrayList<Pair<Int, Int>>
) {

    val direction = instruction.direction
    for (i in 0 until instruction.distance) {
        positions[0] = Pair(
            positions[0].first + direction.toAdd.first,
            positions[0].second + direction.toAdd.second
        )

        for(i in 1 until 10) {
            positions[i] = calcFollowerPosition(positions[i - 1], positions[i], direction)
        }

        visitedSet.add(positions[9])
        println(positions)
        // different row or col, separated by > 1 opposite
        println(positions[0])
    }
}

fun calcFollowerPosition(curHeadPosition: Pair<Int, Int>, curTailPosition: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
    if (
        Math.abs(curHeadPosition.first - curTailPosition.first) <= 1 &&
        Math.abs(curHeadPosition.second - curTailPosition.second) <= 1
    ) {
        return curTailPosition
    } else if (curHeadPosition.first == curTailPosition.first
        || curHeadPosition.second == curTailPosition.second
    ) {
        // same row or col
        val toMove = calcRowCol(curHeadPosition, curTailPosition)

        return Pair(
            curTailPosition.first + toMove.first,
            curTailPosition.second + toMove.second
        )
    } else {
        val toMove = calcDiag(curHeadPosition, curTailPosition)
        return Pair(curTailPosition.first + toMove.first, curTailPosition.second + toMove.second)
    }
}

data class Instruction(val direction: Direction, val distance: Int)

enum class Direction(val toAdd: Pair<Int, Int>) {
    UP(Pair(0, 1)),
    DOWN(Pair(0, -1)),
    LEFT(Pair(-1, 0)),
    RIGHT(Pair(1, 0)),
}

fun Direction.inverse(): Direction {
    return when(this) {
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT -> Direction.LEFT
    }
}

fun calcRowCol(headPos: Pair<Int, Int>, tailPos: Pair<Int, Int>): Pair<Int, Int> {
    return if(headPos.first != tailPos.first) {
        if(headPos.first > tailPos.first) {
            Pair(1, 0)
        } else {
            Pair(-1, 0)
        }
    } else {
        if(headPos.second > tailPos.second) {
            Pair(0, 1)
        } else {
            Pair(0, -1)
        }
    }
}

fun calcDiag(headPos: Pair<Int, Int>, tailPos: Pair<Int, Int>): Pair<Int, Int> {
    val xDiff = headPos.first - tailPos.first
    val yDiff = headPos.second - tailPos.second
    return if(headPos.first > tailPos.first) {
        if (headPos.second > tailPos.second) {
            Pair(1, 1)
        } else {
            Pair(1, -1)
        }
    } else {
        if(headPos.second > tailPos.second) {
            Pair(-1, 1)
        } else {
            Pair(-1, -1)
        }
    }
}