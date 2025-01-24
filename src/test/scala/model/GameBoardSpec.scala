package model

import model.GameboardComponent.GameBaseImpl.GameBoard
import model.PlayerComponent.PlayerInterface
import model.GameboardComponent.GameBaseImpl.Value.*
import model.GameboardComponent.GameBaseImpl.shipModel.ShipInterface
import model.GameboardComponent.{CellInterface, GameBoardInterface}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import model.GameboardComponent.GameBaseImpl.Cell

class GameBoardSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A GameBoard" should {

    val mockPlayer = mock[PlayerInterface]
    val mockShip = mock[ShipInterface]
    val gameBoard = new GameBoard(5) // Creating a 5x5 board

    // Mock ship size
    when(mockShip.sizeOf()).thenReturn(3)

    "place a ship on valid positions" in {
      // Valid positions for ship placement
      val positions = List((1, 1), (1, 2), (1, 3))

      // Call placeShip method
      val updatedBoard = gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Verify ship placement (the value in the cells should be X)
      updatedBoard.getCellValue(1, 1) shouldEqual X
      updatedBoard.getCellValue(1, 2) shouldEqual X
      updatedBoard.getCellValue(1, 3) shouldEqual X
    }

    "not place a ship if the positions are invalid (out of bounds)" in {
      // Invalid positions (out of bounds)
      val invalidPositions = List((5, 5), (5, 6), (5, 7))

      // Call placeShip method
      val updatedBoard = gameBoard.placeShip(mockPlayer, Some(mockShip), invalidPositions, Cell(X))

      // Ensure the board is not updated (cell value should be ☐)
      updatedBoard.getCellValue(5, 5) shouldEqual ☐
    }

    "not place a ship if the positions overlap" in {
      // Valid positions for ship placement
      val positions = List((1, 1), (1, 2), (1, 3))

      // Place a ship at positions
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Attempt to place another ship at the same positions
      val updatedBoard = gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(O))

      // Ensure the original ship still occupies the positions (X should remain)
      updatedBoard.getCellValue(1, 1) shouldEqual X
      updatedBoard.getCellValue(1, 2) shouldEqual X
      updatedBoard.getCellValue(1, 3) shouldEqual X
    }

    "ensure the ship placement is valid when positions are in the same row or column" in {
      // Valid horizontal placement
      val horizontalPositions = List((1, 1), (1, 2), (1, 3))

      // Valid vertical placement
      val verticalPositions = List((1, 1), (2, 1), (3, 1))

      // Check that the ship can be placed in both horizontal and vertical
      gameBoard.isPlacementValid(mockShip, horizontalPositions) shouldEqual true
      gameBoard.isPlacementValid(mockShip, verticalPositions) shouldEqual true
    }

    "ensure the ship placement is invalid if positions are not aligned" in {
      // Invalid placement (not all positions are aligned in a row or column)
      val invalidPositions = List((1, 1), (2, 2), (3, 3))

      gameBoard.isPlacementValid(mockShip, invalidPositions) shouldEqual false
    }

    "hit a ship correctly" in {
      // First, place a ship on the board
      val positions = List((1, 1), (1, 2), (1, 3))
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Test a successful hit
      gameBoard.hit((1, 1)) shouldEqual true

      // Test a missed hit (hitting an empty cell)
      gameBoard.hit((0, 0)) shouldEqual false
    }

    "return the correct positions of placed ships" in {
      // Place a ship
      val positions = List((1, 1), (1, 2), (1, 3))
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Ensure the correct ship positions are returned
      gameBoard.getShipPositions should contain allOf ((1, 1), (1, 2), (1, 3))
    }

    "allow the board to be cleaned" in {
      // Place a ship before cleaning
      val positions = List((1, 1), (1, 2), (1, 3))
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Clean the board
      gameBoard.clean()

      // Ensure all cells are reset to ☐
      gameBoard.getCellValue(1, 1) shouldEqual ☐
      gameBoard.getCellValue(1, 2) shouldEqual ☐
      gameBoard.getCellValue(1, 3) shouldEqual ☐
    }

    "copy the board correctly" in {
      // Place a ship
      val positions = List((1, 1), (1, 2), (1, 3))
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Copy the board
      val copiedBoard = gameBoard.copyBoard()

      // Ensure the copied board has the same state as the original
      copiedBoard.getCellValue(1, 1) shouldEqual X
      copiedBoard.getCellValue(1, 2) shouldEqual X
      copiedBoard.getCellValue(1, 3) shouldEqual X
    }

    "return correct attack positions" in {
      // Place ships on the board
      val positions = List((1, 1), (1, 2), (1, 3))
      gameBoard.placeShip(mockPlayer, Some(mockShip), positions, Cell(X))

      // Check the attack positions
      val attackPositions = gameBoard.getAttackPositions
      attackPositions should contain allOf ((1, 1), (1, 2), (1, 3))
    }
  }
}
