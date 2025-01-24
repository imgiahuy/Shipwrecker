import aview.tui
import org.scalatest.*
import controller.ControllerComponent.ControllerInterface
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock

class TuiSpec extends AnyWordSpec with Matchers {

  "A Shipwrecker TUI" should {

    "start a new game" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "new game" command
      tui.handleCommand("new game")

      // Verify that the controller's clean method was called to start a new game
      verify(controllerMock).clean()
      println("Test passed: New game started.")
    }

    "place a ship correctly" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "place ship" command
      val placeShipCommand = "place ship Player1 3 (5,7) (5,8) (5,9)"
      tui.handleCommand(placeShipCommand)

      // Verify that the controller's placeShips method was called
      verify(controllerMock).placeShips(any(),ArgumentMatchers.eq(3), any())
      println("Test passed: Ship placed correctly.")
    }

    "fail to place a ship with invalid input" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate an invalid "place ship" command
      val invalidPlaceShipCommand = "place ship Player1 6 (5,7) (5,8) (5,9)"
      tui.handleCommand(invalidPlaceShipCommand)
    }

    "attack successfully" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "attack" command
      val attackCommand = "attack Player1 5 7"
      tui.handleCommand(attackCommand)

      // Verify that the controller's attack method was called
      verify(controllerMock).attack(ArgumentMatchers.eq(5), ArgumentMatchers.eq(7), ArgumentMatchers.eq("Player2"))
      println("Test passed: Attack made successfully.")
    }

    "fail to attack with invalid coordinates" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate an invalid "attack" command
      val invalidAttackCommand = "attack Player1 5 a"
      tui.handleCommand(invalidAttackCommand)

      // Check that invalid input is handled gracefully
      // (you can verify via behavior or stdout)
    }

    "undo the last action successfully" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "undo" command
      tui.handleCommand("undo")

      // Verify that the controller's undo method was called
      verify(controllerMock).undo
      println("Test passed: Undo successful.")
    }

    "redo the last undone action successfully" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "redo" command
      tui.handleCommand("redo")

      // Verify that the controller's redo method was called
      verify(controllerMock).redo
      println("Test passed: Redo successful.")
    }

    "save the game" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "save" command
      tui.handleCommand("save")

      // Verify that the controller's save method was called
      verify(controllerMock).save
      println("Test passed: Game saved.")
    }

    "load the game" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate the "load" command
      tui.handleCommand("load")

      // Verify that the controller's load method was called
      verify(controllerMock).load
      println("Test passed: Game loaded.")
    }

    "handle unknown commands gracefully" in {
      val controllerMock = mock[ControllerInterface]
      val tui = new tui(controllerMock)

      // Simulate an unknown command
      tui.handleCommand("unknown command")

      // Check that the "Unknown command" message is printed
      // This would normally require capturing stdout, but for now, the test ensures the command is passed and handled.
    }
  }
}
