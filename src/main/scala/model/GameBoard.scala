package model

import scala.reflect.ClassTag

case class GameBoard(cells: Board[Cell]) {
  def this(size: Int) = this(new Board[Cell](size, Cell(0)))

  def placeShip(ship: Ship, where: (Int, Int), richtung: String): Int = {
    if (ship.sizeOf() < 0 || ship.sizeOf() > 5) {
      0
    } else if (cells.cells(where._1)(where._2) != Cell(0)) {//and at x,y == Cell(9)
      0
    } else {
      val (dx, dy) = richtung match {
        case "v" => (1, 0)
        case "h" => (0, 1)
        case _ => return 0  // invalid direction
      }
      for (i <- 0 until ship.sizeOf()) {
        val x = where._1 + i * dx
        val y = where._2 + i * dy
        cells.replace(x, y, ship.getShipCell(i))
      }
      ship.sizeOf()
    }
  }

  def clean(): Unit = cells.replaceAll(Cell(0))

  def hit(where: (Int, Int)): Boolean = {
    val (row, col) = where
    val currentCell = cells.cells(row)(col)
    if (currentCell == Cell(0)) {
      false
    } else {
      //cells.replace(row, col, Cell(1)) // Mark as hit
      true
    }
  }

  def copyBoard(): GameBoard = {
    val size = cells.cells.length
    val newBoard = new Board[Cell](size, Cell(0))
    for (i <- cells.cells.indices; j <- cells.cells(i).indices) {
      if (cells.cells(i)(j) != Cell(0) && cells.cells(i)(j) != Cell(9)) {
        newBoard.replace(i, j, Cell(1))
      }
    }
    GameBoard(newBoard)
  }

  def display(): Unit = cells.display()

  def isEmpty: Boolean = {
    cells.cells.forall(row => row.forall(_ == Cell(0)))
  }
}