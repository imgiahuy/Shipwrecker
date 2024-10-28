package controller

import util.Observable
import model.*
import model.Value.O

class Controller(b1: GameBoard, b2: GameBoard, show: GameBoard, b1_blank: GameBoard, b2_blank: GameBoard,
                 player1: Player, player2: Player) extends Observable {
  
  private var remainingShips: Map[String, Int] = Map(
    player1.name -> player1.numShip,
    player2.name -> player2.numShip
  )
  
  def clean(): Unit = {
    b1.clean()
    b2.clean()
    show.clean()
    b1_blank.clean()
    b2_blank.clean()
    remainingShips = Map(
      player1.name -> player1.numShip,
      player2.name -> player2.numShip
    )
    notifyObservers()
  }

  def placeShips(playerName: String, shipSize: Int, pox: Int, poy: Int, direction: String): Boolean = {
    val (board, player) = playerName match {
      case name if name == player1.name => (b1, player1)
      case name if name == player2.name => (b2, player2)
      case _ => throw new IllegalArgumentException("Unknown player name")
    }

    // Try placing the ship on the board
    val placementSuccess = if (shipSize >= 2 && shipSize <= 5) {
      board.placeShip(new Ship(shipSize, Cell(O)), (pox, poy), direction) != 0
    } else {
      false
    }
    
    if (placementSuccess) {
      remainingShips = remainingShips.updated(player.name, remainingShips(player.name) - 1)
      notifyObservers()
    }
    placementSuccess
  }

  def solver() : Int = {
    val solver = Solver()
    if (solver.solved(b1_blank.copyBoard(), b2.copyBoard())) {
      2
    } else if (solver.solved(b2_blank.copyBoard(), b1.copyBoard())) {
      1
    } else {
      3
    }
  }

  def getNamePlayer1: String = {
    player1.name
  }

  def getNamePlayer2: String = {
    player2.name
  }

  def showMe(): Unit = {
    show.display()
  }

  def boardShow(player: String): Unit = {
    if (player == player1.name) {
      b1.display()
    }
    if (player == player2.name) {
      b2.display()
    }
  }

  def blankBoardShow(player: String): Unit = {
    if (player == player1.name) {
      b1_blank.display()
    }
    if (player == player2.name) {
      b2_blank.display()
    }
  }

  def getNumShip(playerName: String): Int = {
    remainingShips.getOrElse(playerName, 0)
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
      myBoard.cells.replace(pox, poy, Cell(Value.O))
      notifyObservers()
      true
    } else {
      myBoard.cells.replace(pox, poy, Cell(Value.X))
      notifyObservers()
      false
    }
  }
}
