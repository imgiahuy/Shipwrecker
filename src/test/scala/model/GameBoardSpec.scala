package model

import model.*
import model.Value.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameBoardSpec extends AnyWordSpec with Matchers {

  "A GameBoard" should {

    "be initialized with the correct size and default cells" in {
      val gameBoard = new GameBoard(4)
      val expectedBoard = Array(
        Array(Cell(☐), Cell(☐), Cell(☐), Cell(☐)),
        Array(Cell(☐), Cell(☐), Cell(☐), Cell(☐)),
        Array(Cell(☐), Cell(☐), Cell(☐), Cell(☐)),
        Array(Cell(☐), Cell(☐), Cell(☐), Cell(☐))
      )
      gameBoard.cells.cells shouldEqual expectedBoard
    }

    "not place a ship when ship size > 5 or ship size < 2" in {
      val ship = new Ship(6, Cell(O))
      val ship2 = new Ship(-1,Cell(O))
      val gameBoard = new GameBoard(5)
      gameBoard.placeShip(ship,(1,1), "h") should be(0)
      gameBoard.placeShip(ship2, (1,1), "h") should be(0)
    }

    "place a ship vertically on the board" in {
      val gameBoard = new GameBoard(5)
      val ship = new Ship(3, Cell(O)) // Ship with 3 cells
      gameBoard.placeShip(ship, (1, 1), richtung = "v") // Horizontal placement at (1,1)
      gameBoard.cells.cells(1)(1) shouldEqual Cell(Value.O)
      gameBoard.cells.cells(2)(1) shouldEqual Cell(Value.O)
      gameBoard.cells.cells(3)(1) shouldEqual Cell(Value.O)
    }

    "place a ship horizontally on the board" in {
      val gameBoard = new GameBoard(5)
      val ship = new Ship(2, Cell(O)) // Ship with 2 cells
      gameBoard.placeShip(ship, (0, 0), richtung = "h") // Vertical placement at (0,0)
      gameBoard.cells.cells(0)(0) shouldEqual Cell(O)
      gameBoard.cells.cells(0)(1) shouldEqual Cell(O)
    }

    "not place a ship if the starting cell is occupied" in {
      val gameBoard = new GameBoard(4)
      val ship1 = new Ship(2, Cell(O))
      gameBoard.placeShip(ship1, (1, 1), richtung = "h")

      val ship2 = new Ship(3, Cell(O))
      gameBoard.placeShip(ship2, (1, 1), richtung = "h") shouldEqual 0 // Ship should not be placed
    }

    "hit a ship and mark it as hit" in {
      val gameBoard = new GameBoard(4)
      val ship = new Ship(2, Cell(O))
      gameBoard.placeShip(ship, (0, 0), richtung = "v")

      gameBoard.hit((0, 0)) shouldEqual true
    }

    "miss a ship if hitting an empty cell" in {
      val gameBoard = new GameBoard(4)
      gameBoard.hit((0, 0)) shouldEqual false
    }

    "clean the board by resetting all cells" in {
      val gameBoard = new GameBoard(4)
      val ship = new Ship(2, Cell(O))
      gameBoard.placeShip(ship, (0, 0), richtung = "h")

      gameBoard.clean()
      gameBoard.cells.cells.flatten shouldEqual Array.fill(16)(Cell(Value.☐)) // All cells should be reset to Cell(0)
    }

    "copy the current state of the board" in {
      val gameBoard = new GameBoard(3)
      val ship = new Ship(2, Cell(O))
      gameBoard.placeShip(ship, (0, 0), richtung = "v")

      val copiedBoard = gameBoard.copyBoard()
      copiedBoard.cells.cells(0)(0) shouldEqual Cell(O)
      copiedBoard.cells.cells(1)(0) shouldEqual Cell(O)
      copiedBoard.cells.cells(2)(0) shouldEqual Cell(☐)
    }
  }
}
