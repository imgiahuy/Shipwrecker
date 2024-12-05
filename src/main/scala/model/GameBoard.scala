package model
import model.Value.{O, X, ☐}
import scala.reflect.ClassTag

case class GameBoard(cells: Board[Cell]) {

  def this(size: Int) = this(new Board[Cell](size, Cell(☐)))

  def placeShip(player: Player, shipOpt: Option[Ship], where: (Int, Int), direction: String, value: Cell): GameBoard = {
    shipOpt match {
      case Some(ship) =>
        val (dx, dy) = direction match {
          case "v" => (1, 0)
          case "h" => (0, 1)
          case _ =>
            println("Invalid direction. Use 'h' for horizontal or 'v' for vertical.")
            return copy(cells)
        }

        if (isPlacementValid(ship, where, dx, dy)) {
          for (i <- 0 until ship.sizeOf()) {
            val x = where._1 + i * dx
            val y = where._2 + i * dy
            cells.replace(x, y, value)
          }
          player.decrease()
        } else {
          println(s"Cannot place ship at (${where._1}, ${where._2}).")
        }
        copy(cells)

      case None =>
        println("Invalid ship size. Ship could not be created.")
        copy(cells)
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

  private def isPlacementValid(ship: Ship, where: (Int, Int), dx: Int, dy: Int): Boolean = {
    val size = ship.sizeOf()
    val (startX, startY) = where

    if (startX < 0 || startY < 0 || startX + dx * (size - 1) >= cells.cells.length || startY + dy * (size - 1) >= cells.cells(0).length) {
      return false
    }

    for (i <- 0 until size) {
      val x = startX + i * dx
      val y = startY + i * dy
      if (cells.cells(x)(y) != Cell(Value.☐)) {
        return false
      }
    }
    true
  }
}