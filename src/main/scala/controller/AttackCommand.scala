package controller

import model.{Cell, Player, Value}
import util.Command

class AttackCommand (player: Player, pox: Int, poy: Int, controller: Controller) extends Command {

  private var previousOpponentCell: Option[Cell] = None
  private var previousBlankCell: Option[Cell] = None
  private var hitSuccessful: Boolean = false

  override def doStep: Unit = {
    // Determine opponent's board and player's blank board
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }

    // Save previous state of the affected cells
    previousOpponentCell = Some(oppBoard.cells.cells(pox)(poy))
    previousBlankCell = Some(myBlankBoard.cells.cells(pox)(poy))

    // Perform the attack
    hitSuccessful = oppBoard.hit(pox, poy)
    myBlankBoard.cells.replace(pox, poy, Cell(if (hitSuccessful) Value.O else Value.X))
  }

  override def undoStep: Unit = {
    // Determine opponent's board and player's blank board
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }

    // Restore the previous state of the affected cells
    previousOpponentCell.foreach(cell => oppBoard.cells.replace(pox, poy, cell))
    previousBlankCell.foreach(cell => myBlankBoard.cells.replace(pox, poy, cell))
  }

  override def redoStep: Unit = {
    // Redo the attack using the saved state
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }

    // Reapply the attack
    oppBoard.hit(pox, poy) // Re-hit to ensure game logic consistency
    myBlankBoard.cells.replace(pox, poy, Cell(if (hitSuccessful) Value.O else Value.X))
  }
}
