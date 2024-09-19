//Test with new approach in creating the board
//This work with the value of the Cell directly

case class Cell(value : Int) {
  //by default Cell = 0
  //0 if no ship
  //else ship
  //toString
  //replace methode maybe ??
}

//a two dimensional array with each element is a Cells
case class Board(cells : Array[Array[Int]]) {
  def this(size : Int, filling: Int) = this(Array.fill(size, size)(filling));
  
  def replace(row : Int, col : Int, value : Int): Unit = {
    cells(row)(col) = value;
  }
}

val board = new Board(12, Cell(0).value)
board.replace(1,1,1);
println(board.cells.map(_.mkString(" ")).mkString("\n"))

