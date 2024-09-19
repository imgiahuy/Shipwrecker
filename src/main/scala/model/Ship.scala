package model

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
