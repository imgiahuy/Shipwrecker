package controller

import model.Value.O
import model.{Cell, Player, SimpleShipFactory, Value}
import util.Command

class PlaceShipCommand(player: Player, shipSize: Int, positions: List[(Int, Int)], controller: Controller) extends Command {

  private var previousCells: List[(Int, Int, Cell)] = List()

  override def doStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2
    val factory = new SimpleShipFactory()
    val shipOpt = factory.createShip(shipSize)

    shipOpt match {
      case Some(ship) =>
        // Save the affected cells before placing the ship
        previousCells = positions.map { case (row, col) => (row, col, board.cells.cells(row)(col)) }

        // Attempt to place the ship using the new placeShip method
        if (player == controller.player1) {
          controller.b1 = controller.b1.placeShip(player, Some(ship), positions, Cell(value = O))
        } else if (player == controller.player2) {
          controller.b2 = controller.b2.placeShip(player, Some(ship),positions, Cell(value = O))
        }

      case None =>
        println(s"Invalid ship size: $shipSize. Could not place ship.")
    }
  }

  override def undoStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2

    // Restore the previous state of the affected cells
    previousCells.foreach { case (row, col, cell) =>
      board.cells.replace(row, col, cell)
    }
    player.increase()
  }

  override def redoStep: Unit = {
    doStep
  }
}
