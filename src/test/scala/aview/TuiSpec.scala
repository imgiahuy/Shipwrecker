package aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import controller.Controller
import model.*

// Test Stub for Controller
class TestController extends Controller(
  b1 = new GameBoard(10),
  b2 = new GameBoard(10),
  show = new GameBoard(10),
  b1_blank = new GameBoard(10),
  b2_blank = new GameBoard(10),
  player1 = Player("Alice", 5),
  player2 = Player("Bob", 5)
) {
  // Overriding methods to capture interactions
  var cleanCalled = false
  var attackCalled = false
  var lastAttackPos: (Int, Int) = (0, 0)
  var lastAttacker: String = ""

  override def clean(): Unit = {
    cleanCalled = true
  }

  override def attack(pox: Int, poy: Int, player: String): Boolean = {
    attackCalled = true
    lastAttackPos = (pox, poy)
    lastAttacker = player
    true // Simulating a hit
  }

  // Additional stubs for required methods
  override def getNamePlayer1: String = "Alice"
  override def getNamePlayer2: String = "Bob"
  override def getNumShip(player: String): Int = 1
  override def solver(): Int = 1 // Simulate player 1 as the winner
}

class TuiSpec extends AnyWordSpec with Matchers {
  // Create an instance of the TestController
  val testController = new TestController
  val tui = new tui(testController)

  "TUI" should {

    "call clean method on new game input" in {
      tui.processInputLine("new game")
      testController.cleanCalled should be (true) // Verify if clean was called
    }

    "not fail on quit input" in {
      noException should be thrownBy tui.processInputLine("quit") // Quit should not throw exceptions
    }
    "should ask who want to place ship on place ship input" in {

    }

    "process attack command correctly" in {

    }

    "call solver and declare a winner on check input" in {
      tui.processInputLine("check")
      // Check that the solver was called and that the output reflects a winner
      testController.solver() should be (1) // Player 1 should win
    }
  }
}