package controller

import model.{Cell, Player, SimpleShipFactory, Value}
import util.Command

class PlaceShipCommand(player: Player, shipSize: Int, pox: Int, poy: Int, direction: String, controller: Controller) extends Command {

  // Save the affected cells' previous states for undo functionality
  private var previousCells: List[(Int, Int, Cell)] = List()

  override def doStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2

    // Save the state of affected cells before placing the ship
    previousCells = getAffectedCells(shipSize, pox, poy, direction).map {
      case (row, col) => (row, col, board.cells.cells(row)(col))
    }

    // Place the ship by modifying the cells on the board
    val ship = SimpleShipFactory().createShip(shipSize)
    if (player == controller.player1) {
      controller.b1 = controller.b1.placeShip(player, ship, (pox, poy), direction, value = Cell(Value.O))
    } else if (player == controller.player2) {
      controller.b2 = controller.b2.placeShip(player, ship, (pox, poy), direction, value = Cell(Value.O))
    }
  }

  override def undoStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2

    // Restore the saved state of affected cells
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
