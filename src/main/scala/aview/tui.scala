package aview

import controller.Controller
import util.Observer

import scala.io.StdIn._

class tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    input match {
      case "new game" =>
        controller.clean()
        println("New game started!")

      case "quit" =>
        println("Game ended. Goodbye!")

      case input if input.startsWith("place ship") =>
        processPlaceShip(input.stripPrefix("place ship").trim)
        println(s"Remaining ships for ${controller.getNamePlayer1}: ${controller.player1.numShip}")
        println(s"Remaining ships for ${controller.getNamePlayer2}: ${controller.player2.numShip}")

      case input if input.startsWith("attack") =>
        processAttack(input.stripPrefix("attack").trim)

      case "check" =>
        val check = controller.solver()
        println(s"Check: ${check.statement}")

      case "undo" =>
        controller.undo
        println("Undo successful.")
        println(s"Remaining ships for ${controller.getNamePlayer1}: ${controller.player1.numShip}")
        println(s"Remaining ships for ${controller.getNamePlayer2}: ${controller.player2.numShip}")

      case "redo" =>
        controller.redo
        println("Redo successful.")

      case "" =>
        println("Input cannot be empty.")

      case _ =>
        println("Unknown command. Please try again.")
    }
  }

  private def processPlaceShip(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length != 5) {
      println("Invalid input format. Use: place ship <player_name> <ship_size> <x> <y> <direction>")
      println("Example: place ship Player1 3 5 7 h")
      return
    }

    val Array(playerName, shipSizeStr, poxStr, poyStr, direction) = args

    // Validate player name
    val player = if (playerName == controller.getNamePlayer1) {
      controller.player1
    } else if (playerName == controller.getNamePlayer2) {
      controller.player2
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

    // Validate coordinates
    val (pox, poy) = (poxStr.toIntOption, poyStr.toIntOption) match {
      case (Some(x), Some(y)) => (x, y)
      case _ =>
        println("Invalid coordinates. Ensure both x and y are integers.")
        return
    }

    // Validate direction
    val dir = direction.toLowerCase
    if (dir != "h" && dir != "v") {
      println("Invalid direction. Use 'h' for horizontal or 'v' for vertical.")
      return
    }

    // Check if the player can place a ship
    if (player.numShip > 0) {
      // Place the ship
      controller.placeShips(player, shipSize, pox, poy, dir)
      println(s"Ship of size $shipSize placed successfully for player $playerName.")
    } else {
      println(s"$playerName, you've already placed all your ships.")
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
      (controller.player1, controller.player2)
    } else if (attackerName == controller.getNamePlayer2) {
      (controller.player2, controller.player1)
    } else {
      println(s"Unknown attacker: $attackerName. Please enter a valid name.")
      return
    }

    // Validate coordinates
    val (pox, poy) = (poxStr.toIntOption, poyStr.toIntOption) match {
      case (Some(x), Some(y)) => (x, y)
      case _ =>
        println("Invalid coordinates. Ensure both x and y are integers.")
        return
    }

    // Perform the attack
    println(s"$attackerName attacks ${defender.name} at ($pox, $poy)!")
    if (controller.attack(pox, poy, defender.name)) {
      println(s"Hit successful at ($pox, $poy)!")
    } else {
      println(s"Missed at ($pox, $poy).")
    }
  }

  override def update(): Unit = {
    println(s"${controller.getNamePlayer1} Board:")
    controller.boardShow(controller.getNamePlayer1)
    println(s"${controller.getNamePlayer1} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer1)

    println(s"${controller.getNamePlayer2} Board:")
    controller.boardShow(controller.getNamePlayer2)
    println(s"${controller.getNamePlayer2} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer2)

    println("\n")
    println(s"Status: ${controller.solver()}")
  }
}
