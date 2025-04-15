package controllerBaseImpl

//something is missing

class SolveCommand (controller: Controller) extends Command {
  var momento_b1: GameBoardInterface = controller.b1
  var momento_b1_blank: GameBoardInterface = controller.b1_blank
  var momento_b2: GameBoardInterface = controller.b2
  var momento_b2_blank: GameBoardInterface = controller.b2_blank

  override def doStep: Unit = {
    momento_b1 = controller.b1
    momento_b1_blank = controller.b1_blank
    momento_b2 = controller.b2
    momento_b2_blank = controller.b2_blank
    val solver = new Solver
    if (solver.solved(controller.b1_blank.copyBoard(), controller.b2.copyBoard())) {
      controller.gameState = PLAYER_1_WIN
    } else if (solver.solved(controller.b2_blank.copyBoard(), controller.b1.copyBoard())) {
      controller.gameState = PLAYER_2_WIN
    } else {
      controller.gameState = CONTINUE
    }
  }

  override def undoStep: Unit = {
    val new_momento_b1 = controller.b1
    val new_momento_b1_blank = controller.b1_blank
    val new_momento_b2 = controller.b2
    val new_momento_b2_blank = controller.b2_blank

    controller.b1 = momento_b1
    controller.b1_blank = momento_b1_blank
    controller.b2 = momento_b2
    controller.b2_blank = momento_b2_blank

    momento_b1 = new_momento_b1
    momento_b1_blank = new_momento_b1_blank
    momento_b2 = new_momento_b2
    momento_b2_blank = new_momento_b2_blank

  }
  override def redoStep: Unit = {
    val new_momento_b1 = controller.b1
    val new_momento_b1_blank = controller.b1_blank
    val new_momento_b2 = controller.b2
    val new_momento_b2_blank = controller.b2_blank

    controller.b1 = momento_b1
    controller.b1_blank = momento_b1_blank
    controller.b2 = momento_b2
    controller.b2_blank = momento_b2_blank

    momento_b1 = new_momento_b1
    momento_b1_blank = new_momento_b1_blank
    momento_b2 = new_momento_b2
    momento_b2_blank = new_momento_b2_blank
  }
}
