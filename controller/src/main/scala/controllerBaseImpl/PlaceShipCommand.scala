package controllerBaseImpl

import GameboardComponent.GameBaseImpl.{Cell, Value}
import GameboardComponent.GameBaseImpl.shipModel.SimpleShipFactory
import PlayerComponent.PlayerInterface
import util.Command

//something is missing


class PlaceShipCommand(player: PlayerInterface, shipSize: Int, positions: List[(Int, Int)], controller: Controller) extends Command {
  
  private var previousCells: List[(Int, Int, Value)] = List()
  
  override def doStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2
    val factory = new SimpleShipFactory()
    val shipOpt = factory.createShip(shipSize)
    shipOpt match {
      case Some(ship) =>
        previousCells = positions.map { case (row, col) => (row, col, board.getCellValue(row,col)) }
        if (player == controller.player1) {
          controller.b1 = controller.b1.placeShip(player, Some(ship), positions, Cell(value = Value.O))
        } else if (player == controller.player2) {
          controller.b2 = controller.b2.placeShip(player, Some(ship),positions, Cell(value = Value.O))
        }
      case None =>
        println(s"Invalid ship size: $shipSize. Could not place ship.")
    }
  }
  
  override def undoStep: Unit = {
    val board = if (player == controller.player1) controller.b1 else controller.b2
    previousCells.foreach { case (row, col, value) =>
      board.updateCell(row, col, value)
    }
    player.increase()
  }
  
  override def redoStep: Unit = doStep
}
