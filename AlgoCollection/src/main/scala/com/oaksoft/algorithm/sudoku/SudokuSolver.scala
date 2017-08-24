package com.oaksoft.algorithm.sudoku

/*
 * Sudoku solver in Scala. 
 * input arg: numbers of the sudoku board in one line for example: 
 * 1,3,5,2,9,7,8,6,4,9,8,2,4,1,6,7,5,3,7,6,4,3,8,5,1,9,2,2,1,8,7,3,9,6,4,5,5,9,7,8,6,4,2,3,1,6,4,3,1,5,2,9,7,8,4,2,6,5,7,1,3,8,9,3,5,9,6,2,8,4,1,7,8,7,1,9,4,3,5,2,6
 * the 1st 9 numbers will be the first row of the sudoku board, the 2nd 9 numbers be the second row and so on ...
 * Output: true if the input sequence of numbers composes a valid Sudoku, false otherwise
 */
object Tester extends App {
  override def main(args: Array[String]) {
    for (ln <- io.Source.stdin.getLines) {
      //println(ln)
      if (isValidSudoku(ln)) {
        val board = parseInput(ln)
        val isBoardVal = isBoardValid(board)
  
        if (isBoardVal) println("True") else println("False")
      } else println("Invlid input")

    }
  }

  def isBoardValid(board: Vector[Vector[Int]]): Boolean = {
    isRowValid(board.transpose) && isColValid(board) && isSubGridsValid(board)
  }

  def isRowValid(board: Vector[Vector[Int]]): Boolean = {
    val dupRows = board.filter { r =>
      r.distinct.size != r.size
    }
    dupRows.isEmpty
  }

  def isColValid(board: Vector[Vector[Int]]): Boolean = {
    isRowValid(board.transpose)
  }

  def isSubGridsValid(board: Vector[Vector[Int]]): Boolean = {
    val ntValidsubGrids = buildSubGrids(board).filterNot(isSubGridValid(_))
    ntValidsubGrids.isEmpty
  }

  //Pre-condition: sbGrid is a 3x3 grid
  def isSubGridValid(sbGrid: Vector[Vector[Int]]): Boolean = {
    val flatV = sbGrid.flatten
    flatV.distinct.size == flatV.size
  }

  def buildSubGrids(board: Vector[Vector[Int]]): Vector[Vector[Vector[Int]]] = {
    val rGroups = board.sliding(3, 3).toVector

    val rs = rGroups.flatMap { rGroup =>
      val rToC = rGroup.transpose
      val tr = rToC.sliding(3, 3)
      tr
    }
    rs
  }

  private def parseInput(ln: String): Vector[Vector[Int]] = {
    val rsl = ln.split(",").sliding(9, 9)
    val rv = for {
      r <- rsl
      rowOfInt = r.map(_.toInt).toVector
    } yield rowOfInt

    rv.toVector
  }
  
  private def isValidSudoku(ln: String) : Boolean = {
    import scala.util.Try
    val inList = ln.split(",")
    if(inList.size !=81) false else {
      val inValidCells = inList.filterNot { cell => 
        Try(cell.toInt).isSuccess && cell.toInt >= 1 &&  cell.toInt <=9
      }
      inValidCells.isEmpty
    }
  }
}
