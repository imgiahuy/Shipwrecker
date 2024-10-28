package controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.*
import model.Value.*
import util.Observer

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

    "clean the boards and reset the number of ships" in {
      // Simulate a game state before cleaning
      b1.placeShip(new Ship(3, Cell(Value.O)), (0, 0), "horizontal")
      b2.placeShip(new Ship(4, Cell(Value.O)), (2, 2), "vertical")
      show.placeShip(new Ship(4, Cell(Value.O)), (2, 2), "vertical")
      b1_blank.placeShip(new Ship(4, Cell(Value.O)), (2, 2), "vertical")
      b2_blank.placeShip(new Ship(4, Cell(Value.O)), (2, 2), "vertical")

      controller.clean()

      b1.isEmpty shouldBe true
      b2.isEmpty shouldBe true
      show.isEmpty shouldBe true
      b1_blank.isEmpty shouldBe true
      b2_blank.isEmpty shouldBe true

      controller.getNumShip(player1.name) shouldEqual player1.numShip
      controller.getNumShip(player2.name) shouldEqual player2.numShip
    }

    "place ships correctly for player1" in {
      val result = controller.placeShips(player1.name,  2, pox = 2, poy = 3, "h")
      result should be (true)  // Assuming ship placement is valid
    }

    "place ships correctly for player2" in {
      val result = controller.placeShips(player2.name, 2, pox = 5, poy = 4, "v")
      result should be (true)  // Assuming ship placement is valid
    }

    "fail to place ships for an unknown player" in {
      assertThrows[IllegalArgumentException] {
        controller.placeShips("UnknownPlayer", 2, pox = 1, poy = 1, "h")
      }
    }

    "fail to place ships for an player with ship size bigger than 5" in {
      val result = controller.placeShips(player1.name, 6, pox = 5, poy = 4, "v")
      result should be (false)
    }

    "fail to place ships for an player with ship size smaller than 2" in {
      val result = controller.placeShips(player1.name, 1, pox = 5, poy = 4, "v")
      result should be(false)
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

    "handle attacks when player name is unknown" in {
      assertThrows[IllegalArgumentException] {
        controller.attack(3, 3, "Unknown Player")
      }
    }

    "handle attacks on hit" in {
      b1.placeShip(new Ship(3, Cell(Value.O)), (0, 0), "h")
      val result = controller.attack(0, 0, player1.name)
      result should be(true)
    }

    "handle board on hit" in {
      b1.placeShip(new Ship(3, Cell(Value.O)), (0, 0), "h")
      val result = controller.attack(0, 0, player1.name)
      b2_blank.isEmpty should be (false)
    }

    "solve the game and determine the winner" in {
      val result = controller.solver()
      result should be >= 1 // It should return 1 or 2, indicating the winning player or 3 for no win
    }

    "update observers on significant events" in {
      var updated = false
      val observer = new Observer {
        override def update(): Unit = updated = true
      }
      controller.add(observer)

      // Trigger observer update
      controller.clean()
      updated shouldBe true
    }

    "display the show board and notify observers in showMe" in {
      var observerNotified = false
      val observer = new Observer {
        override def update(): Unit = observerNotified = true
      }
      controller.add(observer)

      controller.showMe()

      // Check if the show board was displayed (assuming display is a void function)
      // Here, we are just verifying observer notification
      observerNotified shouldBe true
    }

    "display the correct player board and notify observers in boardShow" in {
      var observerNotified = false
      val observer = new Observer {
        override def update(): Unit = observerNotified = true
      }
      controller.add(observer)

      // Display board for player1
      controller.boardShow(player1.name)
      observerNotified shouldBe true
      observerNotified = false // Reset for next test

      // Display board for player2
      controller.boardShow(player2.name)
      observerNotified shouldBe true
    }

    "display the correct blank player board and notify observers in blankBoardShow" in {
      var observerNotified = false
      val observer = new Observer {
        override def update(): Unit = observerNotified = true
      }
      controller.add(observer)

      // Display blank board for player1
      controller.blankBoardShow(player1.name)
      observerNotified shouldBe true
      observerNotified = false // Reset for next test

      // Display blank board for player2
      controller.blankBoardShow(player2.name)
      observerNotified shouldBe true
    }
  }
}
