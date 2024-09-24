import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Board, Cell}

class BoardSpec extends AnyWordSpec with Matchers {
  "A Board" when {
    "created with parameter size and filled with Cell(0)" should {
      val myBoard = new Board(3, Cell(0))
      val expectedArray = Array(
        Array(Cell(0),Cell(0),Cell(0)),
        Array(Cell(0),Cell(0),Cell(0)),
        Array(Cell(0),Cell(0),Cell(0))
      )
      "have expected value" in {
        myBoard.cells shouldEqual(expectedArray)
      }
    }
    "replaced a value with value 5 at (0,0)" should {
      val board_1 = new Board(3, Cell(0))
      board_1.replace(0,0,Cell(5))
      "have expected value" in {
        board_1.cells(0)(0) shouldEqual(Cell(5))
      }
    }
    "replaced all cells with a given value" should {
      val board_1 = new Board(3, Cell(0))
      board_1.replaceAll(Cell(5))
      val expectedArray = Array(
        Array(Cell(5), Cell(5), Cell(5)),
        Array(Cell(5), Cell(5), Cell(5)),
        Array(Cell(5), Cell(5), Cell(5))
      )
      "have expected Array" in {
        board_1.cells should be(expectedArray)
      }
      "display the board correctly" in {
        val board = new Board[Int](2, 1)
        val out = new java.io.ByteArrayOutputStream()
        Console.withOut(out) {
          board.display()
        }
        val output = out.toString.trim
        output shouldEqual "1 1\n1 1"
      }
    }
  }
}
