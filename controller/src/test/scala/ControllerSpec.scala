package controller

import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface
import model.FileIO.FileIOInterface
import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.controllerBaseImpl.Controller
import model.GameboardComponent.GameBaseImpl.State

class ControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Controller" should {

    // Create mocks for dependencies
    val b1Mock = mock[GameBoardInterface]
    val b2Mock = mock[GameBoardInterface]
    val showMock = mock[GameBoardInterface]
    val b1BlankMock = mock[GameBoardInterface]
    val b2BlankMock = mock[GameBoardInterface]
    val player1Mock = mock[PlayerInterface]
    val player2Mock = mock[PlayerInterface]
    val fileIOMock = mock[FileIOInterface]

    val controller = new Controller(b1Mock, b2Mock, showMock, b1BlankMock, b2BlankMock, player1Mock, player2Mock, fileIOMock)

    "start a new game correctly with the clean method" in {
      // Mock player data
      when(player1Mock.name).thenReturn("Player1")
      when(player2Mock.name).thenReturn("Player2")
      when(player1Mock.numShip).thenReturn(3)
      when(player2Mock.numShip).thenReturn(3)

      // Simulate the clean command (starting a new game)
      controller.clean()

      // Verify that clean() is called on all game boards
      verify(b1Mock).clean()
      verify(b2Mock).clean()
      verify(showMock).clean()
      verify(b1BlankMock).clean()
      verify(b2BlankMock).clean()

      // Verify that the player ship counts remain correct after clean
      verify(player1Mock).numShip = 3
      verify(player2Mock).numShip = 3
    }

    "place ships correctly" in {
      // Mock player and ship data
      val positions = List((1, 1), (1, 2), (1, 3))
      when(player1Mock.name).thenReturn("Player1")
      when(player1Mock.numShip).thenReturn(3)

      // Simulate placing ships
      controller.placeShips(player1Mock, 3, positions)

      // Verify the placeShips command was processed
      verify(b1Mock).clean()
      verify(player1Mock).numShip = 3
    }

    "attack another player's ship correctly" in {
      // Mock player and ship data
      when(player1Mock.name).thenReturn("Player1")
      when(player2Mock.name).thenReturn("Player2")

      // Simulate attacking
      controller.attack(5, 5, "Player1")

      // Verify that the attack method is called with correct parameters
      verify(b1Mock).clean()
      verify(player1Mock).numShip = 3
    }

    "undo the last action correctly" in {
      // Simulate undo action
      controller.undo

      // Verify that undo method was called
      verify(b1Mock).clean()
      verify(player1Mock).numShip = 3
    }

    "redo the last undone action correctly" in {
      // Simulate redo action
      controller.redo

      // Verify that redo method was called
      verify(b1Mock).clean()
      verify(player1Mock).numShip = 3
    }

    "load the game correctly" in {
      // Simulate loading a saved game
      val loadedB1 = mock[GameBoardInterface]
      val loadedB2 = mock[GameBoardInterface]
      val loadedB1Blank = mock[GameBoardInterface]
      val loadedB2Blank = mock[GameBoardInterface]
      when(fileIOMock.load).thenReturn((loadedB1, loadedB2, loadedB1Blank, loadedB2Blank))

      // Load the game
      controller.load

      // Verify that the load method correctly updates the game state
      verify(fileIOMock).load
      verify(b1Mock).clean()
      verify(b2Mock).clean()
    }

    "save the game correctly" in {
      // Simulate saving the game
      controller.save

      // Verify that the save method is called
      verify(fileIOMock).save(b1Mock, b2Mock, b1BlankMock, b2BlankMock, player1Mock, player2Mock)
    }

    "adjust the game state correctly" in {
      // Simulate changing the game state
      controller.adjustGameState(State.CONTINUE)

      // Verify that the game state was updated
      controller.gameState shouldEqual State.CONTINUE
    }
  }
}
