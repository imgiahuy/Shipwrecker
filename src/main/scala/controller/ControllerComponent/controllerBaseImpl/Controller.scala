package controller.ControllerComponent.controllerBaseImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.GameState.GameState
import model.*
import model.FileIO.FileIOInterface
import model.GameboardComponent.GameBaseImpl.State.CONTINUE
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface
import util.{Observable, UndoManager}

class Controller @Inject() (var b1: GameBoardInterface, var b2: GameBoardInterface, var show: GameBoardInterface, var b1_blank: GameBoardInterface, var b2_blank: GameBoardInterface,
                            @Named("Player1") var player1: PlayerInterface, @Named("Player2") var player2: PlayerInterface, fileIO: FileIOInterface) extends ControllerInterface {

  private var remainingShips: Map[String, Int] = Map(
    player1.name -> player1.numShip,
    player2.name -> player2.numShip
  )

  private val undoManager = new UndoManager
  var gameState: GameState = CONTINUE

  override def clean(): Unit = {
    List(b1, b2, show, b1_blank, b2_blank).foreach(_.clean())
    player1.shipCounter.update(remainingShips(player1.name))
    player2.shipCounter.update(remainingShips(player2.name))
    notifyObservers
  }

  override def placeShips(player: PlayerInterface, shipSize: Int, positions: List[(Int, Int)]): Unit = {
    undoManager.doStep(new PlaceShipCommand(player, shipSize, positions, this))
    notifyObservers
  }

  override def solver(): Unit = {
    undoManager.doStep(new SolveCommand(this))
    notifyObservers
  }

  override def getNamePlayer1: String = player1.name
  override def getNamePlayer2: String = player2.name
  override def showMe(): Unit = show.display()

  override def boardShow(player: String): Unit = {
    if (player == player1.name) {
      b1.display()
    }
    if (player == player2.name) {
      b2.display()
    }
  }

  override def blankBoardShow(player: String): Unit = {
    if (player == player1.name) {
      b1_blank.display()
    }
    if (player == player2.name) {
      b2_blank.display()
    }
  }

  override def getNumShip(playerName: String): Int = remainingShips.getOrElse(playerName, 0)


  override def attack(pox: Int, poy: Int, player: String): Unit = {
    val attacker = if (player == player1.name) player1 else player2
    undoManager.doStep(new AttackCommand(attacker, pox, poy, this))
    notifyObservers
  }

  override def undo: Unit = {
    undoManager.undoStep
    notifyObservers
  }

  override def redo: Unit = {
    undoManager.redoStep
    notifyObservers
  }
  override def getPlacedShips(player: PlayerInterface): List[(Int, Int)] = {
    val gameBoard = if (player == player1) b1 else b2
    gameBoard.getShipPositions
  }

  override def getAttackShips(player: PlayerInterface): List[(Int, Int)] = {
    val gameBoard = if (player == player1) b1_blank else b2_blank
    gameBoard.getAttackPositions
  }

  override def getPlayer1: PlayerInterface = player1
  override def getPlayer2: PlayerInterface = player2
  override def getGameState: GameState = gameState
  override def adjustGameState(state: GameState) : Unit = gameState = state
  override def getBoard1: GameBoardInterface = b1
  override def getBoard2: GameBoardInterface = b2
  
  override def load: Unit = {
    val (loadedB1, loadedB2, loadedB1Blank, loadedB2Blank, loadPlayer1, loadPlayer2) = fileIO.load
    b1 = loadedB1
    b2 = loadedB2
    b1_blank = loadedB1Blank
    b2_blank = loadedB2Blank
    player1.shipCounter.update(loadPlayer1)
    player2.shipCounter.update(loadPlayer2)
    remainingShips = Map(
      player1.name -> player1.numShip,
      player2.name -> player2.numShip
    )
    notifyObservers
  }

  override def save: Unit = {
    fileIO.save(b1, b2, b1_blank, b2_blank, player1, player2)
    notifyObservers
  }
}
