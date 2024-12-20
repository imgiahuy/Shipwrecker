package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.GameBoardInterface

case class Solver() {
  // Method to compare two GameBoard objects
  def solved(board1: GameBoardInterface, board2: GameBoardInterface): Boolean = {
    // Check if either board is empty (which could mean the game is not over)
    if (board1.isEmpty || board2.isEmpty) {
      return false
    }

    // Check if the boards have the same size
    if (board1.getCellSize != board2.getCellSize) {
      return false
    }

    val size = board1.getCellSize
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        val cell1 = board1.getCellValue(row, col)
        val cell2 = board2.getCellValue(row, col)

        if (cell1 != cell2) {
          return false
        }
      }
    }
    // If all checks pass, the boards are solved
    true
  }
}
