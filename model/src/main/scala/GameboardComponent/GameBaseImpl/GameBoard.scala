package GameboardComponent.GameBaseImpl

import PlayerComponent.PlayerInterface
import GameboardComponent.GameBaseImpl.Value.{O, X, ☐}
import GameboardComponent.GameBaseImpl.shipModel.ShipInterface
import GameboardComponent.{CellInterface, GameBoardInterface}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

case class GameBoard (cells: Board[Cell]) extends GameBoardInterface {
  
  private val FIXED_SIZE : Int = 12

  def this(size: Int) = this(new Board[Cell](size, Cell(☐)))

  override def placeShip(player: PlayerInterface, shipOpt: Option[ShipInterface], positions: List[(Int, Int)], value: CellInterface): GameBoard = {
    val result = for {
      ship <- shipOpt
      _ <- Option.when(ship.sizeOf() >= 2 && ship.sizeOf() <= 5 && positions.size == ship.sizeOf())(())
      _ <- Option.when(isPlacementValid(ship, positions) && positionValid(positions))(())
      cell <- value match {
        case c: Cell => Some(c)
        case _ => None
      }
    } yield {
      val updatedCells = cells.cells.zipWithIndex.map {
        case (row, x) =>
          row.zipWithIndex.map {
            case (c, y) if positions.contains((x, y)) => cell
            case (c, _) => c
          }
      }
      val newBoard = Board(updatedCells)
      player.decrease()
      GameBoard(newBoard)
    }

    result.getOrElse(this)
  }


  // Helper method to check if a ship placement is valid (no overlapping, within bounds)
  override def isPlacementValid(ship: ShipInterface, positions: List[(Int, Int)]): Boolean = {
    positions.forall {
      case (x, y) => isWithinBounds(x, y) && cells.cells(x)(y).value == Value.☐
    }
  }

  override def positionValid(positions: List[(Int, Int)]): Boolean = {
    if (positions.isEmpty || positions.length > 5 || positions.length < 2) {
      return false
    }
    // Check if all positions are in the same row or column
    val rows = positions.map(_._1)
    val cols = positions.map(_._2)

    // Check for horizontal placement (all rows are the same, and columns are continuous)
    if (rows.distinct.size == 1) {
      val sortedCols = cols.sorted
      return sortedCols.sliding(2).forall { case List(a, b) => b - a == 1 }
    }

    // Check for vertical placement (all columns are the same, and rows are continuous)
    if (cols.distinct.size == 1) {
      val sortedRows = rows.sorted
      return sortedRows.sliding(2).forall { case List(a, b) => b - a == 1 }
    }

    // If neither condition is met, the positions are not valid
    false
  }


  // Helper method to check if a position is within the board's bounds
  override def isWithinBounds(x: Int, y: Int): Boolean = {
    x >= 0 && x < cells.numRows && y >= 0 && y < cells.numCols
  }


  override def clean(): Unit = cells.replaceAll(Cell(Value.☐))

  override def hit(where: (Int, Int)): Boolean = {
    val (row, col) = where
    Try(cells.cells(row)(col)) match {
      case Success(cell) =>
        cell.value != Value.☐ && cell.value != Value.X
      case Failure(_) =>
        false // or log the error, or rethrow, depending on your needs
    }
  }

  override def copyBoard(): GameBoard = {
    val size = cells.cells.length
    val newBoard = new Board[Cell](size, Cell(Value.☐))
    for (i <- cells.cells.indices; j <- cells.cells(i).indices) {
      if (cells.cells(i)(j) != Cell(Value.☐) && cells.cells(i)(j) != Cell(Value.X)) {
        newBoard.replace(i, j, Cell(Value.O))
      }
    }
    GameBoard(newBoard)
  }

  override def display(): String = cells.display()

  override def isEmpty: Boolean = {
    cells.cells.forall(row => row.forall(_ == Cell(Value.☐)))
  }
  override def getShipPositions: List[(Int, Int)] = {
    (for {
      row <- cells.cells.indices
      col <- cells.cells(row).indices
      if cells.cells(row)(col).value != Value.☐ && cells.cells(row)(col).value != Value.X
    } yield (row, col)).toList
  }

  override def getAttackPositions: List[(Int, Int)] = {
    cells.cells.indices.flatMap { row =>
      cells.cells(row).indices.collect {
        case col if cells.cells(row)(col).value == Value.O || cells.cells(row)(col).value == Value.X =>
          (row, col)
      }
    }.toList
  }

  override def attack(x: Int, y: Int): Boolean = {
    val hitSuccessful = hit((x, y))
    hitSuccessful
  }
  

  override def updateCell(x: Int, y: Int, value: Value): Unit = {
    cells.replace(x, y, Cell(value))
  }

  override def getCellValue(x: Int, y: Int): Value = {
    Try(cells.cells(x)(y).value).getOrElse(Value.☐)
  }

  override def getCellSize: Int = {
    Try(cells.cells.length).getOrElse(0)
  }

  override def createEmptyBoard: GameBoard = {
    new GameBoard(FIXED_SIZE)
  }
}