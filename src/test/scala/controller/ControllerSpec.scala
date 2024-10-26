package controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.*

class ControllerSpec extends AnyWordSpec with Matchers {

  val player1 = new Player("Alice", 5)  // Dummy Player with name Alice and 5 ships
  val player2 = new Player("Bob", 5)    // Dummy Player with name Bob and 5 ships

  val b1 = new GameBoard(10)            // 10x10 GameBoard for player1
  val b2 = new GameBoard(10)            // 10x10 GameBoard for player2
  val show = new GameBoard(10)          // Show board for display
  val b1_blank = new GameBoard(10)      // Blank board for player1
  val b2_blank = new GameBoard(10)      // Blank board for player2

  // Create controller with the above dependencies
  val controller = new Controller(b1, b2, show, b1_blank, b2_blank, player1, player2)

  "Controller" should {

    "place ships correctly for player1" in {
      val result = controller.placeShips(player1.name, numShip = 1, pox = 2, poy = 3, richtung = "h")
      result should be (true)  // Assuming ship placement is valid
    }

    "place ships correctly for player2" in {
      val result = controller.placeShips(player2.name, numShip = 1, pox = 5, poy = 4, richtung = "v")
      result should be (true)  // Assuming ship placement is valid
    }

    "fail to place ships for an unknown player" in {
      assertThrows[IllegalArgumentException] {
        controller.placeShips("UnknownPlayer", numShip = 1, pox = 1, poy = 1, richtung = "horizontal")
      }
    }

    "return the correct name of player1" in {
      controller.getNamePlayer1 should be ("Alice")
    }

    "return the correct name of player2" in {
      controller.getNamePlayer2 should be ("Bob")
    }

    "handle attacks correctly for player1" in {
      val result = controller.attack(3, 3, player1.name)
      result should be (false) // Assuming no hit at this position initially
    }

    "handle attacks correctly for player2" in {
      val result = controller.attack(3, 3, player2.name)
      result should be (false) // Assuming no hit at this position initially
    }

    "solve the game and determine the winner" in {
      val result = controller.solver()
      result should be >= 1 // It should return 1 or 2, indicating the winning player or 3 for no win
    }
  }
}
