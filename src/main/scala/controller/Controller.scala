package controller

import util.{Observable, UndoManager}
import model.*
import model.State.{CONTINUE, PLAYER_1_WIN, PLAYER_2_WIN}
import model.Value.O

class Controller(var b1: GameBoard, var b2: GameBoard, var show: GameBoard, var b1_blank: GameBoard, var b2_blank: GameBoard,
                 var player1: Player, var player2: Player) extends Observable {
  
  private var remainingShips: Map[String, Int] = Map(
    player1.name -> player1.numShip,
    player2.name -> player2.numShip
  )

  private val undoManager = new UndoManager
  
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
    notifyObservers
  }

  def placeShips(player: Player, shipSize: Int, pox: Int, poy: Int, direction: String): Unit = {
    undoManager.doStep(new PlaceShipCommand(player, shipSize, pox, poy, direction , this))
    notifyObservers
  }

  def solver(): State = {
    val solver = Solver()
    if (solver.solved(b1_blank.copyBoard(), b2.copyBoard())) {
      PLAYER_1_WIN
    } else if (solver.solved(b2_blank.copyBoard(), b1.copyBoard())) {
      PLAYER_2_WIN
    } else {
      CONTINUE
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
    //player 1 attack player 2
    val (oppBord, myBoard) = if (player == player1.name) {
      (b1, b2_blank)
    } else if (player == player2.name) {
      (b2, b1_blank)
    } else {
      throw new IllegalArgumentException("Unknown player name")
    }
    if(oppBord.hit(pox,poy)) {
      myBoard.cells.replace(pox, poy, Cell(Value.O))
      notifyObservers
      true
    } else {
      myBoard.cells.replace(pox, poy, Cell(Value.X))
      notifyObservers
      false
    }
  }

  def undo: Unit = {
    undoManager.undoStep
    notifyObservers
  }

  def redo: Unit = {
    undoManager.redoStep
    notifyObservers
  }
}
