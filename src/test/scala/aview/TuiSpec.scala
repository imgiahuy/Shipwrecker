package aview
import controller.*
import model.*
import model.GameboardComponent.GameBaseImpl.{Cell, GameBoard, State, Value}
import model.PlayerComponent.Player
import Value.{O, X}
import controller.ControllerComponent.controllerBaseImpl.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec  extends AnyWordSpec with Matchers {
  "A Shipwrecker TUI" should {

    val player1 = new Player("Alice", 5) // Dummy Player with name Alice and 5 ships
    val player2 = new Player("Bob", 0) // Dummy Player with name Bob and 0 ships

    val b1 = new GameBoard(10) // 10x10 GameBoard for player1
    val b2 = new GameBoard(10) // 10x10 GameBoard for player2
    val show = new GameBoard(10) // Show board for display
    val b1_blank = new GameBoard(10) // Blank board for player1
    val b2_blank = new GameBoard(10) // Blank board for player2

    // Create controller with the above dependencies
    val controller = new Controller(b1, b2, show, b1_blank, b2_blank, player1, player2)

    val tui = new tui(controller)

    "do nothing on input 'quit'" in {
      tui.processInputLine("quit")
    }

    "create new game by cleaning everything" in {
      tui.processInputLine("new game")
      b1.isEmpty should be (true)
      b2.isEmpty should be(true)
      show.isEmpty should be(true)
      b1_blank.isEmpty should be(true)
      b2_blank.isEmpty should be (true)
      player1.numShip should be (5)
      player2.numShip should be(0)
    }

    " place ship correctly with correct input" in {
      val input = "place ship Alice 2 0 0 h"
      tui.processInputLine(input)
      b1.cells.cells(0)(0) should be (Cell(O))
      b1.cells.cells(0)(1) should be (Cell(Value.O))
    }

    " do nothing if place ship input is incorrect" in {
      val input = "place ship Alice 2 0 h"
      tui.processInputLine(input)
    }

    " not place ship if the ship size is not valid" in {
      val input = "place ship Alice 7 0 0 h"
      tui.processInputLine(input)
    }

    " not place ship if player has no ship left" in {
      val input = "place ship Bob 2 0 0 h"
      tui.processInputLine(input)
    }

    " attack ship with correct input (on hit)" in {
      controller.placeShips("Alice", 2, 1, 1, "h")
      val input = "attack Bob 1 1"
      tui.processInputLine(input)
      b2_blank.cells.cells(1)(1) should be (Cell(O))
    }
    " attack ship with correct input (on miss)" in {
      val input = "attack Bob 2 2"
      tui.processInputLine(input)
      b2_blank.cells.cells(2)(2) should be(Cell(X))
    }

    " not attack if the attack input is incorrect" in {
      val input = "attack Bob"
      tui.processInputLine(input)
    }

    " check when input is check (Bob won the game)" in {
      controller.clean()
      controller.placeShips("Alice", 2, 1, 1, "h")
      controller.attack(1, 1, "Alice")
      controller.attack(1, 2, "Alice")

      controller.boardShow("Alice")
      tui.processInputLine("check")
      controller.solver() should be (State.PLAYER_2_WIN)
    }
    " check when input is check (Game not finish)" in {
      controller.clean()
      tui.processInputLine("check")
      controller.solver() should be (State.CONTINUE)
    }

    }
}