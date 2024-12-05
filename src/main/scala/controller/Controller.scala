package controller

import controller.GameState.GameState
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
  var gameState: GameState = CONTINUE
  
  def clean(): Unit = {
    b1.clean()
    b2.clean()
    show.clean()
    b1_blank.clean()
    b2_blank.clean()
    player1.numShip = player1.numShip + (remainingShips(player1.name) - player1.numShip)
    player2.numShip = player2.numShip + (remainingShips(player2.name) - player2.numShip)

    // Update the remainingShips map to match
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

  def solver(): Unit = {
    undoManager.doStep(new SolveCommand(this))
    notifyObservers
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

  def attack(pox: Int, poy: Int, player: String): Unit = {
    val attacker = if (player == player1.name) player1 else player2
    undoManager.doStep(new AttackCommand(attacker, pox, poy, this))
    notifyObservers
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
