package model

import scala.util.boundary

case class Solver() {
  // Method to compare two GameBoard objects
  def solved(board1: GameBoard, board2: GameBoard): Boolean = {
    // Use boundary block to allow early exit
    boundary {
      // Step 1: Check if the boards are of the same size
      if (board1.cells.cells.length != board2.cells.cells.length) {
        boundary.break(false) // Exit early if board sizes differ
      }

      // Step 2: Compare each cell in both boards
      for (i <- board1.cells.cells.indices) {
        for (j <- board1.cells.cells(i).indices) {
          // Compare the content of the cells at position (i, j)
          if (board1.cells.cells(i)(j) != board2.cells.cells(i)(j)) {
            boundary.break(false) // Exit early if any cells are not identical
          }
        }
      }

      // Step 3: If no differences are found, return true
      true
    }
  }
}
