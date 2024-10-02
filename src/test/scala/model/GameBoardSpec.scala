package model

import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameBoardSpec extends AnyWordSpec with Matchers {

  "A GameBoard" should {

    "be initialized with the correct size and default cells" in {
      val gameBoard = new GameBoard(4)
      val expectedBoard = Array(
        Array(Cell(0), Cell(0), Cell(0), Cell(0)),
        Array(Cell(0), Cell(0), Cell(0), Cell(0)),
        Array(Cell(0), Cell(0), Cell(0), Cell(0)),
        Array(Cell(0), Cell(0), Cell(0), Cell(0))
      )
      gameBoard.cells.cells shouldEqual expectedBoard
    }

    "not place a ship when ship size > 5 or ship size < 0" in {
      val ship = new Ship(6, Cell(2))
      val ship2 = new Ship(-1,Cell(2))
      val gameBoard = new GameBoard(5)
      gameBoard.placeShip(ship,(1,1), "h") should be(0)
      gameBoard.placeShip(ship2, (1,1), "h") should be(0)
    }

    "place a ship horizontally on the board" in {
      val gameBoard = new GameBoard(5)
      val ship = new Ship(3, Cell(2)) // Ship with 3 cells
      gameBoard.placeShip(ship, (1, 1), richtung = "h") // Horizontal placement at (1,1)
      gameBoard.cells.cells(1)(1) shouldEqual Cell(2)
      gameBoard.cells.cells(2)(1) shouldEqual Cell(2)
      gameBoard.cells.cells(3)(1) shouldEqual Cell(2)
    }

    "place a ship vertically on the board" in {
      val gameBoard = new GameBoard(5)
      val ship = new Ship(2, Cell(3)) // Ship with 2 cells
      gameBoard.placeShip(ship, (0, 0), richtung = "v") // Vertical placement at (0,0)
      gameBoard.cells.cells(0)(0) shouldEqual Cell(3)
      gameBoard.cells.cells(0)(1) shouldEqual Cell(3)
    }

    "not place a ship if the starting cell is occupied" in {
      val gameBoard = new GameBoard(4)
      val ship1 = new Ship(2, Cell(4))
      gameBoard.placeShip(ship1, (1, 1), richtung = "h")

      val ship2 = new Ship(3, Cell(5))
      gameBoard.placeShip(ship2, (1, 1), richtung = "h") shouldEqual 0 // Ship should not be placed
    }

    "hit a ship and mark it as hit" in {
      val gameBoard = new GameBoard(4)
      val ship = new Ship(2, Cell(3))
      gameBoard.placeShip(ship, (0, 0), richtung = "h")

      gameBoard.hit((0, 0)) shouldEqual true
      gameBoard.cells.cells(0)(0) shouldEqual Cell(1) // Hit should change cell to Cell(1)
    }

    "miss a ship if hitting an empty cell" in {
      val gameBoard = new GameBoard(4)
      gameBoard.hit((0, 0)) shouldEqual false
      gameBoard.cells.cells(0)(0) shouldEqual Cell(0) // Cell remains unchanged
    }

    "clean the board by resetting all cells" in {
      val gameBoard = new GameBoard(4)
      val ship = new Ship(2, Cell(3))
      gameBoard.placeShip(ship, (0, 0), richtung = "h")

      gameBoard.clean()
      gameBoard.cells.cells.flatten shouldEqual Array.fill(16)(Cell(0)) // All cells should be reset to Cell(0)
    }

    "copy the current state of the board" in {
      val gameBoard = new GameBoard(3)
      val ship = new Ship(2, Cell(5))
      gameBoard.placeShip(ship, (0, 0), richtung = "h")

      val copiedBoard = gameBoard.copyBoard()
      copiedBoard.cells.cells(0)(0) shouldEqual Cell(1)
      copiedBoard.cells.cells(1)(0) shouldEqual Cell(1)
      copiedBoard.cells.cells(2)(0) shouldEqual Cell(0)
    }

    "display the board correctly" in {
      val gameBoard = new GameBoard(3)
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        gameBoard.display()
      }
      val output = out.toString.trim
      output shouldEqual "0 0 0\n0 0 0\n0 0 0"
    }
  }
}
