package model

import scala.reflect.ClassTag

case class Board[Cell : ClassTag](cells : Array[Array[Cell]]) {
  def this(size: Int, filling: Cell) = this(Array.fill(size, size)(filling))
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
    println(cells.map(_.mkString(" ")).mkString("\n"))
  }
}