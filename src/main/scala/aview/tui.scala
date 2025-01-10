package aview

import controller.ControllerComponent.ControllerInterface


import scala.io.StdIn.*

class tui(controller: ControllerInterface) extends TuiTemplate(controller) {

  // Implement the command handling logic
  override def handleCommand(command: String): Unit = {
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

      case _ =>
        println("Unknown command. Please try again.")
    }
  }

  private def processPlaceShip(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length < 3) {
      println("Invalid input format. Use: place ship <player_name> <ship_size> <positions>")
      println("Example: place ship Player1 3 (5,7) (5,8) (5,9)")
      return
    }

    val Array(playerName, shipSizeStr, positionsStr @_*) = args

    // Validate player name
    val player = if (playerName == controller.getNamePlayer1) {
      controller.getPlayer1
    } else if (playerName == controller.getNamePlayer2) {
      controller.getPlayer2
    } else {
      println(s"Unknown player: $playerName. Please enter a valid name.")
      return
    }

    // Validate ship size
    val shipSize = shipSizeStr.toIntOption match {
      case Some(size) if size >= 2 && size <= 5 => size
      case _ =>
        println("Invalid ship size. Use one of: 2, 3, 4, 5")
        return
    }
    // Parse positions
    val positions = positionsStr.map(parsePosition).toList

    // Check if the ship can be placed
    if (player.numShip > 0) {
      // Place the ship
      controller.placeShips(player, shipSize, positions)
    } else {
      println(s"$playerName, you've already placed all your ships.")
    }
  }

  // Parse position string like (5,7) to (5, 7)
  private def parsePosition(posStr: String): (Int, Int) = {
    val pattern = """\((\d+),\s*(\d+)\)""".r
    posStr match {
      case pattern(x, y) => (x.toInt, y.toInt)
      case _ =>
        println(s"Invalid position format: $posStr. Expected format is (x, y).")
        (-1, -1) // Invalid position
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

    // Validate attacker name
    val (attacker, defender) = if (attackerName == controller.getNamePlayer1) {
      (controller.getPlayer1, controller.getPlayer2)
    } else if (attackerName == controller.getNamePlayer2) {
      (controller.getPlayer2, controller.getPlayer1)
    } else {
      println(s"Unknown attacker: $attackerName. Please enter a valid name.")
      return
    }

    val (pox, poy) = (poxStr.toIntOption, poyStr.toIntOption) match {
      case (Some(x), Some(y)) => (x, y)
      case _ =>
        println("Invalid coordinates. Ensure both x and y are integers.")
        return
    }

    // Perform the attack
    println(s"$attackerName attacks ${defender.name} at ($pox, $poy)!")
    controller.attack(pox, poy, defender.name)
  }
}
