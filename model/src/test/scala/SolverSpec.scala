package model

import model.GameboardComponent.GameBaseImpl.Solver
import model.GameboardComponent.GameBoardInterface
import model.GameboardComponent.GameBaseImpl.Value.*
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class SolverSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Solver" should {

    "return false if either board is empty" in {
      // Create two mocked game boards
      val board1 = mock[GameBoardInterface]
      val board2 = mock[GameBoardInterface]

      // Mock the isEmpty method to simulate an empty board
      when(board1.isEmpty).thenReturn(true)
      when(board2.isEmpty).thenReturn(false)

      val solver = Solver()

      // Test the solved method
      solver.solved(board1, board2) shouldEqual false
    }

    "return false if the boards have different sizes" in {
      // Create two mocked game boards
      val board1 = mock[GameBoardInterface]
      val board2 = mock[GameBoardInterface]

      // Mock the getCellSize method to return different sizes for the boards
      when(board1.getCellSize).thenReturn(5)
      when(board2.getCellSize).thenReturn(6)

      val solver = Solver()

      // Test the solved method
      solver.solved(board1, board2) shouldEqual false
    }

    "return false if the boards have different cell values" in {
      // Create two mocked game boards
      val board1 = mock[GameBoardInterface]
      val board2 = mock[GameBoardInterface]

      // Mock the getCellSize method to return the same size for both boards
      when(board1.getCellSize).thenReturn(3)
      when(board2.getCellSize).thenReturn(3)

      // Mock the getCellValue method to return different values for a cell
      when(board1.getCellValue(0, 0)).thenReturn(X)
      when(board2.getCellValue(0, 0)).thenReturn(O)

      val solver = Solver()

      // Test the solved method
      solver.solved(board1, board2) shouldEqual false
    }

    "return true if the boards have the same cell values and size" in {
      // Create two mocked game boards
      val board1 = mock[GameBoardInterface]
      val board2 = mock[GameBoardInterface]

      // Mock the getCellSize method to return the same size for both boards
      when(board1.getCellSize).thenReturn(3)
      when(board2.getCellSize).thenReturn(3)

      // Mock the getCellValue method to return the same value for both boards
      when(board1.getCellValue(0, 0)).thenReturn(X)
      when(board2.getCellValue(0, 0)).thenReturn(X)

      when(board1.getCellValue(0, 1)).thenReturn(O)
      when(board2.getCellValue(0, 1)).thenReturn(O)

      when(board1.getCellValue(1, 0)).thenReturn(☐)
      when(board2.getCellValue(1, 0)).thenReturn(☐)

      val solver = Solver()

      // Test the solved method
      solver.solved(board1, board2) shouldEqual true
    }
  }
}
