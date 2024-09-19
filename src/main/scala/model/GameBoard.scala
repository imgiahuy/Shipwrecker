package model

import scala.reflect.ClassTag

case class GameBoard(cells : Board[Cell]) {
  def this(size: Int) = this(new Board[Cell](size, Cell(0)))

  def placeShip(ship: Ship, where: (Int, Int), richtung: Boolean): Int = {
    //Richtung == True -> horizontal -> x + 1
    //Richtung == False -> vertical -> y + 1
    if (ship.sizeOf() < 0 || ship.sizeOf() > 5) {
      0
    } else if (cells.cells(where._1)(where._2) != Cell(0)) {
      0
    } else {
      if (richtung) {
        var x = where._1
        var i = 0
        while (i < ship.sizeOf()) {
          cells.replace(x, where._2, ship.getShipCell(i))
          x = x + 1
          i = i + 1
        }
      } else if (!richtung) {
        var y = where._2
        var i = 0
        while (i < ship.sizeOf()) {
          cells.replace(where._1, y, ship.getShipCell(i))
          y = y + 1
          i = i + 1
        }
      }
      ship.sizeOf()
    }
  } //which is used to place the ship

  def clean() : Unit = cells.replaceAll(Cell(0))

  def hit(where : (Int, Int)): Boolean = {
    val row = where._1
    val col = where._2
    val currentCell = cells.cells(row)(col)
    if (currentCell == Cell(0)) {
      return false
    } else if (currentCell != Cell(0)) {
      cells.replace(row, col, Cell(1))
      return true
    }
    false
  }
  def copyBoard() : GameBoard = {
    val size = cells.cells.length
    val newBoard = new Board[Cell](size, Cell(0))
    for (i <- cells.cells.indices) {
      for (j <- cells.cells(i).indices) {
        if (cells.cells(i)(j) != Cell(0)) {
          newBoard.replace(i, j, Cell(1))
        }
      }
    }
    GameBoard(newBoard)
  }
  def display(): Unit = {
    cells.display()
  }
}
