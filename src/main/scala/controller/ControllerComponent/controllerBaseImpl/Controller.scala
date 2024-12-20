package controller.ControllerComponent.controllerBaseImpl

import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.GameState.GameState
import model.*
import model.GameboardComponent.GameBaseImpl.State.CONTINUE
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface
import util.{Observable, UndoManager}

class Controller(var b1: GameBoardInterface, var b2: GameBoardInterface, var show: GameBoardInterface, var b1_blank: GameBoardInterface, var b2_blank: GameBoardInterface,
                 var player1: PlayerInterface, var player2: PlayerInterface) extends ControllerInterface {

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

  def placeShips(player: PlayerInterface, shipSize: Int, positions: List[(Int, Int)]): Unit = {
    undoManager.doStep(new PlaceShipCommand(player, shipSize, positions, this))
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
  def getPlacedShips(player: PlayerInterface): List[(Int, Int)] = {
    val gameBoard = if (player == player1) b1 else b2
    gameBoard.getShipPositions
  }

  def getAttackShips(player: PlayerInterface): List[(Int, Int)] = {
    val gameBoard = if (player == player1) b1_blank else b2_blank
    gameBoard.getAttackPositions
  }
}
