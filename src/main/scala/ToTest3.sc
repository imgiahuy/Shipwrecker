import model.Cell

import scala.reflect.ClassTag
//Test with new approach in creating the board
//This will work with the object Cell
case class Cell(value : Int) {
  //by default Cell = 0
  //0 if no ship
  //else ship
  override def toString: String = value.toString;
  //replace methode maybe ??
}

//a two dimensional array with each element is a Cells
case class Board[Cell : ClassTag](cells : Array[Array[Cell]]) {
  def this(size : Int, filling: Cell) = this(Array.fill(size, size)(filling));

  def replace(row : Int, col : Int, value : Cell): Unit = {
    cells(row)(col) = value;
  }
  def display() : Unit = {
    println(cells.map(_.mkString(" ")).mkString("\n"))
  }
}

case class gameBoard(cells : Board[Cell]) {
  def this(size: Int) = this(new Board[Cell](size, Cell(0)))
  def placeShip() : Unit = {} //which is used to place the ship
  def attack() : Unit = {}
  def display() : Unit = {cells.display()}
}

val board = new Board(12, Cell(0))

board.replace(1,1,Cell(1));
board.display();

case class player (name : String, numShip : Int)

case class ship (shipCell : Array[Cell]) {
  //size can only be 2,3,4,5 means :
  //2 Cells, 3 Cells, 4 Cells, 5 Cells
  def this(size : Int, filling : Cell) = this(Array.fill(size)(filling))
  def sizeOf() : Int = {
    shipCell.length
  }
  def getShipCell(index : Int) : Cell = {
    shipCell.apply(index)
  }
  override def toString = shipCell.mkString("Array(", ", ", ")");
}

val boardgames = new gameBoard(9)
val myship = new ship(2,Cell(2));
myship.getShipCell(1)

println(myship)
myship.sizeOf()
boardgames.display()

//result of each hit, turn ??
case class gone() {}
