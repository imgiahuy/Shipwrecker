package model

import model.Value.{O, X, ☐}

import scala.reflect.ClassTag

case class GameBoard(cells: Board[Cell]) {
  
  def this(size: Int) = this(new Board[Cell](size, Cell(☐)))

  def placeShip(ship: Ship, where: (Int, Int), richtung: String): Int = {
    if (ship.sizeOf() < 0 || ship.sizeOf() > 5) {
      0
    } else if (cells.cells(where._1)(where._2) != Cell(Value.☐)) {//and at x,y == Cell(9)
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
        cells.replace(x, y, Cell(Value.O))
      }
      ship.sizeOf()
    }
  }

  def clean(): Unit = cells.replaceAll(Cell(Value.☐))

  def hit(where: (Int, Int)): Boolean = {
    val (row, col) = where
    val currentCell = cells.cells(row)(col)
    if (currentCell == Cell(Value.☐) || currentCell == Cell(Value.X) ) {
      false
    } else {
      true
    }
  }

  def copyBoard(): GameBoard = {
    val size = cells.cells.length
    val newBoard = new Board[Cell](size, Cell(Value.☐))
    for (i <- cells.cells.indices; j <- cells.cells(i).indices) {
      if (cells.cells(i)(j) != Cell(Value.☐) && cells.cells(i)(j) != Cell(Value.X)) {
        newBoard.replace(i, j, Cell(Value.O))
      }
    }
    GameBoard(newBoard)
  }

  def display(): Unit = cells.display()

  def isEmpty: Boolean = {
    cells.cells.forall(row => row.forall(_ == Cell(Value.☐)))
  }
}