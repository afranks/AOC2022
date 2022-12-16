import java.io.File

fun main() {
    val lines = File("Day8.csv").useLines { it.toList() }
    val grid = ArrayList<ArrayList<Int>>()

    for(l in lines) {
        val lGrid = ArrayList<Int>()
        grid.add(lGrid)
        for(c in l) {
            lGrid.add(c - '0')
        }
    }

//    var set = HashSet<Pair<Int, Int>>()
//    var count = 0
//    // From up
//    for(i in 0 until grid.first().size) {
//        var curMax = -1
//        for(j in 0 until grid.size) {
//            if(grid[j][i] > curMax) {
//                ++count
//                set.add(Pair(j,i))
//            }
//            curMax = Math.max(curMax, grid[j][i])
//        }
//    }
//    // from down
//    for(i in 0 until grid.first().size) {
//        var curMax = -1
//        for(j in grid.size - 1 downTo  0) {
//            if(grid[j][i] > curMax) {
//                ++count
//                set.add(Pair(j,i))
//            }
//            curMax = Math.max(curMax, grid[j][i])
//        }
//    }
//
//    //from left
//    for(i in 0 until grid.first().size) {
//        var curMax = -1
//        for(j in 0 until grid.size) {
//            if(grid[i][j] > curMax) {
//                ++count
//                set.add(Pair(i,j))
//            }
//            curMax = Math.max(curMax, grid[i][j])
//        }
//    }
//
//    // from right
//    for(i in 0 until grid.size) {
//        var curMax = -1
//        for(j in grid.first().size - 1 downTo  0) {
//            if(grid[i][j] > curMax) {
//                ++count
//                set.add(Pair(i,j))
//            }
//            curMax = Math.max(curMax, grid[i][j])
//        }
//    }
//
//
////    println(grid)
//    println(count)
//    println(set.size)
//    println(set)

    var max = 0

    for(i in 0 until grid.size) {
        for(j in 0 until grid.first().size) {
            max = Math.max(calcVisible(grid[i][j], grid, Pair(j, i)), max)
        }
    }
    println(max)
}

fun calcVisible(curTree: Int, array: ArrayList<ArrayList<Int>>, curCoords: Pair<Int, Int>): Int {

    // up
    var upCount = 0
    for(i in curCoords.second - 1 downTo 0) {
        upCount++
        if(array[i][curCoords.first] >= curTree) {
            break
        }
    }

    //down
    var downCount = 0

    for(i in curCoords.second + 1 until array.size) {
            downCount++


        if(array[i][curCoords.first] >= curTree) {
            break
        }
    }

    var leftCount = 0

    // left
    for(j in curCoords.first - 1 downTo 0) {
            leftCount++


        if(array[curCoords.second][j] >= curTree) {
            break
        }
    }

    // right
    var rightCount = 0

    for(j in curCoords.first + 1 until array.first().size) {
            rightCount++

        if(array[curCoords.second][j] >= curTree) {
            break
        }
    }
    println(curCoords)
    println(upCount * downCount * rightCount * leftCount)
    return upCount * downCount * rightCount * leftCount
}