//something is missing

import scala.io.StdIn.*
import scala.concurrent.Promise

class tui(controller: ControllerInterface) extends Observer {

  controller.add(this)

  def processInput(input: String): Unit = {
    input.trim match {
      case "" => handleEmptyInput()
      case "quit" => handleQuit()
      case command => handleCommand(command)
    }
  }

  def handleEmptyInput(): Unit = println("Input cannot be empty.")

  def handleQuit(): Unit = println("Game ended. Goodbye!")

  def handleCommand(command: String): Unit = {
    command match {
      case "new game" =>
        controller.clean()
        println("New game started!")

      case input if input.startsWith("place ship") =>
        processPlaceShip(input.stripPrefix("place ship").trim)
        println(s"Remaining ships for ${controller.getNamePlayer1}: ${controller.getPlayer1.numShip}")
        println(s"Remaining ships for ${controller.getNamePlayer2}: ${controller.getPlayer2.numShip}")

      case input if input.startsWith("attack") =>
        processAttack(input.stripPrefix("attack").trim)

      case "undo" =>
        controller.undo
        println("Undo successful.")
        println(s"Remaining ships for ${controller.getNamePlayer1}: ${controller.getPlayer1.numShip}")
        println(s"Remaining ships for ${controller.getNamePlayer2}: ${controller.getPlayer2.numShip}")

      case "redo" =>
        controller.redo
        println("Redo successful.")

      case "check" =>
        val check = controller.solver()
        println(s"Check result: $check")

      case "load" =>
        controller.load
        println("Game loaded.")

      case "save" =>
        controller.save
        println("Game saved.")

      case _ =>
        println("Unknown command. Please try again.")
    }
  }

  // --- Observer Update ---
  override def update(): Unit = {
    displayBoards()
    showGameState()
  }

  def displayBoards(): Unit = {
    println(s"${controller.getNamePlayer1} Board:")
    controller.boardShow(controller.getNamePlayer1)
    println(s"${controller.getNamePlayer1} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer1)

    println(s"${controller.getNamePlayer2} Board:")
    controller.boardShow(controller.getNamePlayer2)
    println(s"${controller.getNamePlayer2} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer2)
  }

  def showGameState(): Unit = {
    println("\n")
    println(GameState.message(controller.getGameState))
  }

  val guiUpdatePromise: Promise[Unit] = Promise()

  // --- Helpers ---
  private def validatePlayer(playerName: String): Option[PlayerInterface] = {
    if (playerName == controller.getNamePlayer1) Some(controller.getPlayer1)
    else if (playerName == controller.getNamePlayer2) Some(controller.getPlayer2)
    else None
  }

  private def validateShipSize(shipSizeStr: String): Option[Int] = {
    shipSizeStr.toIntOption.filter(size => size >= 2 && size <= 5)
  }

  private def parsePosition(posStr: String): Option[(Int, Int)] = {
    val pattern = """\((\d+),\s*(\d+)\)""".r
    posStr match {
      case pattern(x, y) => Some((x.toInt, y.toInt))
      case _ => None
    }
  }

  private def processPlaceShip(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length < 3) {
      println("Invalid input format. Use: place ship <player_name> <ship_size> <positions>")
      println("Example: place ship Player1 3 (5,7) (5,8) (5,9)")
      return
    }

    val Array(playerName, shipSizeStr, positionsStr@_*) = args

    validatePlayer(playerName) match {
      case Some(player) =>
        validateShipSize(shipSizeStr) match {
          case Some(shipSize) =>
            val positions = positionsStr.flatMap(parsePosition).toList
            if (player.numShip > 0) {
              controller.placeShips(player, shipSize, positions)
            } else {
              println(s"$playerName, you've already placed all your ships.")
            }
          case None =>
            println("Invalid ship size. Use one of: 2, 3, 4, 5")
        }
      case None =>
        println(s"Unknown player: $playerName. Please enter a valid name.")
    }
  }

  private def validateAttacker(attackerName: String): Option[(PlayerInterface, PlayerInterface)] = {
    if (attackerName == controller.getNamePlayer1) {
      Some(controller.getPlayer1, controller.getPlayer2)
    } else if (attackerName == controller.getNamePlayer2) {
      Some(controller.getPlayer2, controller.getPlayer1)
    } else {
      None
    }
  }

  private def processAttack(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length != 3) {
      println("Invalid input format. Use: attack <attacker_name> <x> <y>")
      println("Example: attack Player1 5 7")
      return
    }

    val Array(attackerName, poxStr, poyStr) = args

    validateAttacker(attackerName) match {
      case Some((attacker, defender)) =>
        val (pox, poy) = (poxStr.toIntOption, poyStr.toIntOption) match {
          case (Some(x), Some(y)) => (x, y)
          case _ =>
            println("Invalid coordinates. Ensure both x and y are integers.")
            return
        }

        println(s"$attackerName attacks ${defender.name} at ($pox, $poy)!")
        controller.attack(pox, poy, defender.name)

      case None =>
        println(s"Unknown attacker: $attackerName. Please enter a valid name.")
    }
  }
}
