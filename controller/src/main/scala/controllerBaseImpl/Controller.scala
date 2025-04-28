package controllerBaseImpl

import FileIOJSON.FileIOInterface
import GameboardComponent.GameBaseImpl.State.CONTINUE
import GameboardComponent.GameBoardInterface
import PlayerComponent.PlayerInterface
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import controllerBaseImpl.GameState.GameState
import util.UndoManager

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}


class Controller (var b1: GameBoardInterface, var b2: GameBoardInterface, var show: GameBoardInterface, var b1_blank: GameBoardInterface, var b2_blank: GameBoardInterface,
                  var player1: PlayerInterface, var player2: PlayerInterface, fileIO: FileIOInterface)(implicit system: ActorSystem, executionContext: ExecutionContext) extends ControllerInterface {

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
  }

  override def placeShips(player: PlayerInterface, shipSize: Int, positions: List[(Int, Int)]): Unit = {
    undoManager.doStep(new PlaceShipCommand(player, shipSize, positions, this))
  }

  override def solver(): Unit = {
    undoManager.doStep(new SolveCommand(this))
  }

  override def getNamePlayer1: String = player1.name
  override def getNamePlayer2: String = player2.name
  override def showMe(): Unit = show.display()

  override def boardShow(player: String): String = {
    if (player == getNamePlayer1) b1.display()
    else if (player == getNamePlayer2) b2.display()
    else "Unknown player"
  }

  override def blankBoardShow(player: String): String = {
    if (player == getNamePlayer1) b1_blank.display()
    else if (player == getNamePlayer2) b2_blank.display()
    else "Unknown player"
  }

  override def getNumShip(playerName: String): Int = remainingShips.getOrElse(playerName, 0)


  override def attack(pox: Int, poy: Int, player: String): Unit = {
    val attacker = if (player == player1.name) player1 else player2
    undoManager.doStep(new AttackCommand(attacker, pox, poy, this))
  }

  override def undo: Unit = {
    undoManager.undoStep
  }

  override def redo: Unit = {
    undoManager.redoStep
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
  }

  override def save: Unit = {
    fileIO.save(b1, b2, b1_blank, b2_blank, player1, player2)
  }
}
