package model

import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SolverSpec extends AnyWordSpec with Matchers {

  "Solver" should {
    "return true for identical boards" in {
      val board1 = new GameBoard(12)

      board1.cells.replace(1,1,Cell(1))

      val board2 = new GameBoard(12)

      board2.cells.replace(1, 1, Cell(1))


      val solver = Solver()
      solver.solved(board1, board2) should be(true)
    }

    "return false for boards with different dimensions" in {
      val board1 = new GameBoard(12)
      val board2 = new GameBoard(11)

      val solver = Solver()
      solver.solved(board1, board2) should be(false)
    }

    "return false for boards with different values" in {
      val board1 = new GameBoard(12)
      val board2 = new GameBoard(12)
      val ship = new Ship(3, Cell(1))
      board2.placeShip(ship, (1, 1), "h")

      val solver = Solver()
      solver.solved(board1, board2) should be(false)

    }
  }
}
