package model.GameboardComponent.GameBaseImpl

import scala.reflect.ClassTag

case class Board [Cell : ClassTag](cells : Array[Array[Cell]]) {
  def this(size: Int, filling: Cell) = {
    this(Array.fill(size, size)(filling))
  }
  def numRows: Int = cells.length
  def numCols: Int = if (numRows > 0) cells(0).length else 0 // Assuming all rows have the same number of columns
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
    println(s"Displaying board with dimensions: ${cells.length} x ${cells(0).length}")
    cells.zipWithIndex.foreach { case (row, rowIndex) =>
      println(s"Row $rowIndex: ${row.mkString(" | ")}")
    }
  }
}