import model._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameBoardGeneratorSpec extends AnyWordSpec with Matchers {

  "A Game board Generator" should {
    "create a default game board when call the Generator of size 12" in {
      val myGameGenerator = GameBoardGenerator(5)
      val myGame = myGameGenerator.boardDefault()
      myGame.cells.cells.length shouldEqual 12
      myGame.cells.cells.forall(_.length == 12) shouldEqual true
    }
    "create a custom game board when call the methode custom" in {
        val myGenerator = GameBoardGenerator(5)
        val myGame = myGenerator.boardCustom()
        myGame.cells.cells.length shouldEqual 5
        myGame.cells.cells.forall(_.length == 5) shouldEqual true
    }
    "generate the default board independently from the custom size" in {
      val generator = GameBoardGenerator(5) // size of 5 provided
      val defaultGameBoard = generator.boardDefault()

      defaultGameBoard.cells.cells.length shouldEqual 12 // Default should be 12
      defaultGameBoard.cells.cells.forall(_.length == 12) shouldEqual true
    }
  }
}
