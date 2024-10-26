package model

case class Solver() {
  // Method to compare two GameBoard objects
  def solved(board1: GameBoard, board2: GameBoard): Boolean = {
    // Check if either board is empty (which could mean the game is not over)
    if (board1.isEmpty || board2.isEmpty) {
      return false
    }

    // Check if the boards have the same size
    if (board1.cells.cells.length != board2.cells.cells.length) {
      return false
    }

    // Iterate over each cell and compare the boards
    for (i <- board1.cells.cells.indices) {
      for (j <- board1.cells.cells(i).indices) {
        val cell1 = board1.cells.cells(i)(j)
        val cell2 = board2.cells.cells(i)(j)

        // Example condition: Cells should match based on hits
        // You can refine this based on your specific game logic (e.g., consider 'hit' states)
        if (cell1 != cell2) {
          return false
        }
      }
    }

    // If all checks pass, the boards are solved
    true
  }
}
