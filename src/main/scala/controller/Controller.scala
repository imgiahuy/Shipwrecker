package controller

import util.Observable
import model.*
import model.State.{CONTINUE, PLAYER_1_WIN, PLAYER_2_WIN}
import model.Value.O

import scala.language.postfixOps

class Controller(b1: GameBoard, b2: GameBoard, show: GameBoard, b1_blank: GameBoard, b2_blank: GameBoard,
                 player1: Player, player2: Player) extends Observable {
  
  private var remainingShips: Map[String, Int] = Map(
    player1.name -> player1.numShip,
    player2.name -> player2.numShip
  )

  //to save ship coordinate
  var downShip: Map[String, Map[model.Ship, Array[(Int, Int)]]] = Map(
    player1.name -> Map[model.Ship, Array[(Int, Int)]](),
    player2.name -> Map[model.Ship, Array[(Int, Int)]]()
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
      val currentShip = SimpleShipFactory().createShip(shipSize)
      board.placeShip(currentShip, (pox, poy), direction)
      downShip = downShip.updated(playerName, downShip(playerName) + (currentShip -> board.shipCoordinate))
      true
    } else {
      false
    }
    
    if (placementSuccess) {
      remainingShips = remainingShips.updated(player.name, remainingShips(player.name) - 1)
      notifyObservers()
    }
    placementSuccess
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

  def formatDownShipForDisplay(downShip: Map[String, Map[Ship, Array[(Int, Int)]]]): String = {
    downShip.map { case (playerName, ships) =>
      val playerShips = if (ships.isEmpty) {
        "  No ships placed\n."
      } else {
        ships.map { case (ship, coordinates) =>
          val coordString = coordinates.map { case (x, y) => s"($x, $y)" }.mkString(", ")
          s"  - Ship(Size: ${ship.sizeOf})"
        }.mkString("\n")
      }
      s"Player: $playerName\n$playerShips"
    }.mkString("\n\n")
  }

  def attack(pox: Int, poy: Int, player: String): Boolean = {
    val (oppBord, myBoard, opponentName) = if (player == player1.name) {
      (b1, b2_blank, player2.name)
    } else if (player == player2.name) {
      (b2, b1_blank, player1.name)
    } else {
      throw new IllegalArgumentException("Unknown player name")
    }
    if(oppBord.hit(pox,poy)) {
      myBoard.cells.replace(pox, poy, Cell(Value.O))
      downShip(opponentName).foreach { case (ship, coordinates) =>
        if (coordinates.contains((pox, poy))) {
          val updatedShip = ship.updateShip()
          downShip = downShip.updated(opponentName, downShip(opponentName).updated(updatedShip, coordinates.filterNot(_ == (pox, poy))))
            if (updatedShip.isSunk) {
              downShip = downShip.updated(opponentName, downShip(opponentName).removed(updatedShip))
            }
        }
      }
      notifyObservers()
      true
    } else {
      myBoard.cells.replace(pox, poy, Cell(Value.X))
      notifyObservers()
      false
    }
  }
}
