package controller

import model.{Cell, Player, SimpleShipFactory, Value}
import util.Command

class PlaceShipCommand(player: Player, shipSize: Int, pox: Int, poy: Int, direction: String, controller: Controller) extends Command {

  private var previousCells: List[(Int, Int, Cell)] = List()

  override def doStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2
    val factory = new SimpleShipFactory()
    val shipOpt = factory.createShip(shipSize)

    shipOpt match {
      case Some(ship) =>
        previousCells = getAffectedCells(ship.sizeOf(), pox, poy, direction).map {
          case (row, col) => (row, col, board.cells.cells(row)(col))
        }

        if (player == controller.player1) {
          controller.b1 = controller.b1.placeShip(player, Some(ship), (pox, poy), direction, value = Cell(Value.O))
        } else if (player == controller.player2) {
          controller.b2 = controller.b2.placeShip(player, Some(ship), (pox, poy), direction, value = Cell(Value.O))
        }

      case None =>
        println(s"Invalid ship size: $shipSize. Could not place ship.")
    }
  }

  override def undoStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2

    previousCells.foreach { case (row, col, cell) =>
      board.cells.replace(row, col, cell)
    }
    player.increase()
  }

  override def redoStep: Unit = {
    doStep
  }

  private def getAffectedCells(shipSize: Int, pox: Int, poy: Int, direction: String): List[(Int, Int)] = {
    if (direction == "h") {
      (0 until shipSize).map(i => (pox, poy + i)).toList
    } else {
      (0 until shipSize).map(i => (pox + i, poy)).toList
    }
  }
}
