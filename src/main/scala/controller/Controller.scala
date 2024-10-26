package controller

import util.Observable
import model._

class Controller(b1: GameBoard, b2: GameBoard, show: GameBoard, b1_blank: GameBoard, b2_blank: GameBoard,
                 player1: Player, player2: Player) extends Observable {

  def clean(): Unit = {
    b1.clean()
    b2.clean()
    show.clean()
    b1_blank.clean()
    b2_blank.clean()
    notifyObservers()
  }

  def placeShips(playerName: String, numShip : Int, pox: Int, poy: Int, richtung: String): Boolean = {
    val board = if (playerName == player1.name) {
      b1
    } else if (playerName == player2.name) {
      b2
    } else {
      throw new IllegalArgumentException("Unknown player name")
    }
    if (board.placeShip(new Ship(numShip, Cell(numShip)), (pox, poy), richtung) != 0) {
      notifyObservers()
      true
    } else {
      notifyObservers()
      false
    }
  }

  def createBoard(size : Int): Unit = {
    val game = new GameBoard(size)
    notifyObservers()
  }

  def solver() : Int = {
    val solver = Solver()
    if (solver.solved(b1_blank.copyBoard(), b2.copyBoard())) {
      notifyObservers()
      2
    } else if (solver.solved(b2_blank.copyBoard(), b1.copyBoard())) {
      notifyObservers()
      1
    } else {
      notifyObservers()
      3
    }
  }

  def getNamePlayer1: String = {
    notifyObservers()
    player1.name
  }

  def getNamePlayer2: String = {
    notifyObservers()
    player2.name
  }

  def showMe(): Unit = {
    show.display()
    notifyObservers()
  }

  def boardShow(player: String): Unit = {
    if (player == player1.name) {
      b1.display()
    }
    if (player == player2.name) {
      b2.display()
    }
    notifyObservers()
  }

  def blankBoardShow(player: String): Unit = {
    if (player == player1.name) {
      b1_blank.display()
    }
    if (player == player2.name) {
      b2_blank.display()
    }
    notifyObservers()
  }

  def getNumShip(player : String): Int = {
    if (player == player1.name) {
      player1.numShip
    } else if (player == player2.name) {
      player2.numShip
    } else {
      0
    }
  }

  def attack(pox: Int, poy: Int, player: String): Boolean = {
    val (oppBord, myBoard) = if (player == player1.name) {
      (b1, b2_blank)
    } else if (player == player2.name) {
      (b2, b1_blank)
    } else {
      throw new IllegalArgumentException("Unknown player name")
    }
    if(oppBord.hit(pox,poy)) {
      myBoard.cells.replace(pox, poy, Cell(1))
      notifyObservers()
      true
    } else {
      myBoard.cells.replace(pox, poy, Cell(9))
      notifyObservers()
      false
    }
  }
}
