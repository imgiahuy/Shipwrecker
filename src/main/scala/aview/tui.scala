package aview
import controller.Controller
import util.Observer

import scala.io.StdIn.*

class tui(controller : Controller) extends Observer {

  controller.add(this)

  val size = 12


  def processInputLine(input: String): Unit = {

    input match {
      case "new game" => controller.clean()

      case "quit" =>

      case input if input.startsWith("place ship") =>
        // Remove "place ship" prefix and trim the remaining input
        val commandArgs = input.stripPrefix("place ship").trim
        val args = commandArgs.split(" ")

        // Check if we have the correct number of arguments
        if (args.length != 5) {
          println("Invalid input format. Please enter: <player_name> <ship_size> <x> <y> <direction>")
          println("Example: place ship Player1 3 5 7 h")
          return
        }

        // Extract arguments
        val Array(name, shipSizeStr, poxStr, poyStr, direction) = args

        // Validate player name
        if (name != controller.getNamePlayer1 && name != controller.getNamePlayer2) {
          println("Unknown player name. Please enter a valid name.")
          return
        }

        // Validate and parse ship size
        val shipSize = shipSizeStr match {
          case "2" => 2
          case "3" => 3
          case "4" => 4
          case "5" => 5
          case _ =>
            println("Invalid ship size. Please enter one of: 2, 3, 4, 5")
            return
        }

        // Validate and parse x and y coordinates
        val pox = scala.util.Try(poxStr.toInt).getOrElse({
          println("Invalid x coordinate. Please enter a valid number.")
          return
        })

        val poy = scala.util.Try(poyStr.toInt).getOrElse({
          println("Invalid y coordinate. Please enter a valid number.")
          return
        })

        // Validate direction
        val dir = direction.toLowerCase
        if (dir != "h" && dir != "v") {
          println("Invalid direction. Please enter 'h' for horizontal or 'v' for vertical.")
          return
        }

        // Check if player has ships left to place
        if (controller.getNumShip(name) > 0) {
          // Attempt to place the ship
          val placementSuccess = controller.placeShips(name, shipSize, pox, poy, dir)
          if (placementSuccess) {
            println(s"Ship of size $shipSize placed successfully for player $name.")
          } else {
            println("Failed to place the ship. The position might be occupied or invalid.")
          }
        } else {
          println(s"$name, you've already placed all your ships.")
        }

      case input if input.startsWith("attack") =>
        // Remove "attack" prefix and trim the remaining input
        val commandArgs = input.stripPrefix("attack").trim
        val args = commandArgs.split(" ")

        // Check if we have the correct number of arguments
        if (args.length != 3) {
          println("Invalid input format. Please enter: <attacker_name> <x> <y>")
          println("Example: attack Player1 5 7")
          return
        }

        // Extract arguments
        val Array(attacker, poxStr, poyStr) = args

        // Validate attacker name
        if (attacker != controller.getNamePlayer1 && attacker != controller.getNamePlayer2) {
          println("Unknown player name. Please enter a valid name.")
          return
        }

        // Determine the defender based on the attacker
        val defender = if (attacker == controller.getNamePlayer1) controller.getNamePlayer2 else controller.getNamePlayer1

        // Validate and parse x and y coordinates
        val pox = scala.util.Try(poxStr.toInt).getOrElse({
          println("Invalid x coordinate. Please enter a valid number.")
          return
        })

        val poy = scala.util.Try(poyStr.toInt).getOrElse({
          println("Invalid y coordinate. Please enter a valid number.")
          return
        })

        // Perform the attack
        print(s"$attacker attacks $defender!\n")

        // Execute the attack
        if (controller.attack(pox, poy, defender)) {
          print(s"Hit successful at ($pox, $poy)\n")
        } else {
          print(s"Hit failed, miss at ($pox, $poy)\n")
        }

      case "check" =>
        val check = controller.solver()
        if(check == 1) {
          print("Game finish!!!\n")
          print(controller.getNamePlayer2 + " has won the game.\n")
        } else if (check == 2) {
          print("Game finish!!!\n")
          print(controller.getNamePlayer1 + " has won the game.\n")
        } else {
          print("Game not finish!!!")
        }
      case "" =>
        println("Input cannot be empty.")
    }
  }
  override def update(): Unit =
    print( s"${controller.getNamePlayer1} Board: \n")
    controller.boardShow(controller.getNamePlayer1)
    print(s"${controller.getNamePlayer1} Blank Board: \n")
    controller.blankBoardShow(controller.getNamePlayer1)

    print( s"${controller.getNamePlayer2} Board: \n")
    controller.boardShow(controller.getNamePlayer2)
    print(s"${controller.getNamePlayer2} Blank Board: \n")
    controller.blankBoardShow(controller.getNamePlayer2)

    print("\n")
    print("Status " + controller.solver() + " \n")
}
