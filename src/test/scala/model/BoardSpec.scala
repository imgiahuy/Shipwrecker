package model

import model.Value.{O, X, ☐}
import model.{Board, Cell}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec with Matchers {
  "A Board" when {
    "created with parameter size and filled with Cell(☐)" should {
      val myBoard = new Board(3, Cell(☐))
      val expectedArray = Array(
        Array(Cell(☐), Cell(☐), Cell(☐)),
        Array(Cell(☐), Cell(☐), Cell(☐)),
        Array(Cell(☐), Cell(☐), Cell(☐))
      )
      "have expected value" in {
        myBoard.cells shouldEqual expectedArray
      }
    }
    "replaced a value with value 5 at (0,0)" should {
      val board_1 = new Board(3, Cell(☐))
      board_1.replace(0, 0, Cell(O))
      "have expected value" in {
        board_1.cells(0)(0) shouldEqual Cell(O)
      }
    }
    "replaced all cells with a given value" should {
      val board_1 = new Board(3, Cell(☐))
      board_1.replaceAll(Cell(X))
      val expectedArray = Array(
        Array(Cell(X), Cell(X), Cell(X)),
        Array(Cell(X), Cell(X), Cell(X)),
        Array(Cell(X), Cell(X), Cell(X))
      )
      "have expected Array" in {
        board_1.cells should be(expectedArray)
      }
    }
    "display the board correctly" in {
      val board = new Board[Int](2,1)
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        board.display()
      }
      val output = out.toString.trim
      output shouldEqual "1 | 1\n1 | 1"
    }
  }
}
