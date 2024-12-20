case class Cell(value : Int) {
  //by default Cell = 0
  //0 if no ship
  //else ship
  override def toString: String = value.toString;
  //replace methode maybe ??
}

case class Ship(shipCell : Array[Cell]) {
  //size can only be 2,3,4,5 means :
  //2 Cells, 3 Cells, 4 Cells, 5 Cells
  def this(size : Int, filling : Cell) = this(Array.fill(size)(filling))
  def sizeOf() : Int = {
    shipCell.length
  }
  def getShipCell(index : Int) : Cell = {
    shipCell.apply(index)
  }
  override def toString: String = shipCell.mkString("Array(", ", ", ")");
}

import model.GameboardComponent.GameBaseImpl.GameBoard
import scala.reflect.ClassTag

case class Board[Cell : ClassTag](cells : Array[Array[Cell]]) {
  def this(size: Int, filling: Cell) = this(Array.fill(size, size)(filling));
  def replace(row: Int, col: Int, value: Cell): Unit = {
      cells(row)(col) = value
  }
  def replaceAll(value : Cell) : Unit = {
    for (i <- cells.indices) {
      for (j <- cells(i).indices) {
        cells(i)(j) = value
      }
    }
  }
  def display(): Unit = {
    println(cells.map(_.mkString(" | ")).mkString("\n"))
  }
}

case class GameBoard(cells : Board[Cell]) {
  def this(size: Int) = this(new Board[Cell](size, Cell(0)))

  def placeShip(ship : Ship, where : (Int, Int), richtung : Boolean): Int = {
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

  def hit(where: (Int, Int)): Boolean = {
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
  def clean() : Unit = {
    cells.replaceAll(Cell(0))
  }

  def copyBoard(): GameBoard = {
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

case class Solver() {
  def solved(board1 : GameBoard, board2 : GameBoard): Boolean = {
    board1 == board2
  }
}

////////////////////////////////////////////////////////////////

val myGame = new GameBoard(12)
myGame.display()

val myShip = new Ship(5, Cell(5))
val myShip1 = new Ship(3, Cell(3))

print(myShip)

myGame.placeShip(myShip, (0,0),true)

myGame.placeShip(myShip1,(1,1),false)

myGame.hit(0,0)
myGame.hit(1,0)

val myRemarked = myGame.copyBoard()
val solver = Solver()

solver.solved(myRemarked, myGame)


myGame.display()

myRemarked.display()