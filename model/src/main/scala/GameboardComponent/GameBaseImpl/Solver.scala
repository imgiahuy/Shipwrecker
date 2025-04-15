package GameboardComponent.GameBaseImpl
import GameboardComponent.GameBoardInterface
case class Solver() {
  def solved(board1: GameBoardInterface, board2: GameBoardInterface): Boolean = {
    // Return false if boards are empty or have different sizes
    if (board1.isEmpty || board2.isEmpty || board1.getCellSize != board2.getCellSize) {
      return false
    }
    // Compare cells of both boards
    (0 until board1.getCellSize).forall { row =>
      (0 until board1.getCellSize).forall { col =>
        board1.getCellValue(row, col) == board2.getCellValue(row, col)
      }
    }
  }
}

