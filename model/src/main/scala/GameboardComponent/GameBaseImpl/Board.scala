package GameboardComponent.GameBaseImpl

import scala.reflect.ClassTag

case class Board [Cell : ClassTag](cells : Array[Array[Cell]]) {
  def this(size: Int, filling: Cell) = {
    this(Array.fill(size, size)(filling))
  }
  def numRows: Int = cells.length
  def numCols: Int = if (numRows > 0) cells(0).length else 0 // Assuming all rows have the same number of columns

  def replace(row: Int, col: Int, value: Cell): Board[Cell] = {
    val newCells = cells.map(_.clone())
    newCells(row)(col) = value
    Board(newCells)
  }

  def replaceAll(value : Cell) : Board[Cell] = {
    val newCells = cells.map(_.map(_ => value))
    Board(newCells)
  }

  def display(): String = {
    val builder = new StringBuilder
    builder.append(s"Displaying board with dimensions: ${cells.length} x ${cells(0).length}\n")

    cells.zipWithIndex.foreach { case (row, rowIndex) =>
      builder.append(s"Row $rowIndex: ${row.mkString(" | ")}\n")
    }

    builder.toString()
  }
}