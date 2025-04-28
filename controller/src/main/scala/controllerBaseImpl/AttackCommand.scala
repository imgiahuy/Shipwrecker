package controllerBaseImpl

import GameboardComponent.GameBaseImpl.Value
import PlayerComponent.PlayerInterface
import util.Command


class AttackCommand (player: PlayerInterface, pox: Int, poy: Int, controller: Controller) extends Command {

  private var previousOpponentCell: Option[Value] = None
  private var previousBlankCell: Option[Value] = None
  private var hitSuccessful: Boolean = false

  override def doStep: Unit = {
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }

    // Save previous state of the affected cells
    previousOpponentCell = Some(oppBoard.getCellValue(pox, poy))
    previousBlankCell = Some(myBlankBoard.getCellValue(pox, poy))

    // Perform the attack
    hitSuccessful = oppBoard.hit(pox, poy)
    myBlankBoard.updateCell(pox, poy, if (hitSuccessful) Value.O else Value.X)
  }

  override def undoStep: Unit = {
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }

    // Restore the previous state of the affected cells
    previousOpponentCell.foreach(value => oppBoard.updateCell(pox, poy, value))
    previousBlankCell.foreach(value => myBlankBoard.updateCell(pox, poy, value))
  }

  override def redoStep: Unit = {
    val (oppBoard, myBlankBoard) = if (player == controller.player1) {
      (controller.b1, controller.b2_blank)
    } else {
      (controller.b2, controller.b1_blank)
    }
    oppBoard.hit(pox, poy) // Re-hit to ensure game logic consistency
    myBlankBoard.updateCell(pox, poy, if (hitSuccessful) Value.O else Value.X)
  }
}
