package model

case class Solver() {
  def solved(board1 : GameBoard, board2 : GameBoard): Boolean = {
    board1 == board2
  }
}
