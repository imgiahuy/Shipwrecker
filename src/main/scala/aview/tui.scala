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

      case "place ship" =>
        println(s"Who gets to place ships first, ${controller.getNamePlayer1} or ${controller.getNamePlayer2}?")
        val input = readLine().trim

        // Validate player name
        val name = if (input == controller.getNamePlayer1) {
          controller.getNamePlayer1
        } else if (input == controller.getNamePlayer2) {
          controller.getNamePlayer2
        } else {
          println("Unknown player name. Please enter a valid name.")
          return
        }

        // Check if player has ships left to place
        if (controller.getNumShip(name) > 0) {
          println("Which ship do you want to place?")
          println("Choose from: 2er, 3er, 4er, 5er")

          val ship = readLine().trim.toLowerCase
          val shipSize = ship match {
            case "2er" => 2
            case "3er" => 3
            case "4er" => 4
            case "5er" => 5
            case _ =>
              println("Invalid ship size. Please enter one of: 2er, 3er, 4er, 5er")
              return
          }

          // Show the board for the player
          controller.showMe()

          // Get the x and y position
          println("Enter the x coordinate for the ship:")
          val pox = scala.util.Try(readInt()).getOrElse({
            println("Invalid x coordinate. Please enter a number.")
            return
          })

          println("Enter the y coordinate for the ship:")
          val poy = scala.util.Try(readInt()).getOrElse({
            println("Invalid y coordinate. Please enter a number.")
            return
          })

          // Get the direction
          println("Place ship horizontally (h) or vertically (v)?")
          val direction = readLine().trim.toLowerCase
          if (direction != "h" && direction != "v") {
            println("Invalid direction. Please enter 'h' for horizontal or 'v' for vertical.")
            return
          }

          // Attempt to place the ship
          val placementSuccess = controller.placeShips(name, shipSize, pox, poy, direction)
          if (placementSuccess) {
            println(s"Ship of size $shipSize placed successfully for player $name.")
          } else {
            println("Failed to place the ship. The position might be occupied or invalid.")
          }
        } else {
          println(s"$name, you've already placed all your ships.")
        }

        // Display the board after the attempted placement
        controller.boardShow(name)

      case "attack" =>
        print("Who attacks? " + controller.getNamePlayer1 + " or " + controller.getNamePlayer2 + "?\n")
        val input = readLine()

        if (input == controller.getNamePlayer1 || input == controller.getNamePlayer2) {
          val attacker = if (input == controller.getNamePlayer1) controller.getNamePlayer1 else controller.getNamePlayer2
          val defender = if (input == controller.getNamePlayer1) controller.getNamePlayer2 else controller.getNamePlayer1
          performAttack(attacker, defender)
        } else {
          print("Invalid input. Please enter a valid player name.\n")
        }
        def performAttack(attacker: String, defender: String): Unit = {
          print(attacker + " attacks " + defender + "!\n")
          controller.blankBoardShow(attacker)
          print("\n")
          print("Attack position x: \n")
          val pox = readInt()
          print("Attack position y: \n")
          val poy = readInt()
          if (controller.attack(pox, poy, defender)) {
            print("Hit successful at (" + pox + " , " + poy + ")\n")
          } else {
            print("Hit failed, miss at (" + pox + " , " + poy + ")\n")
          }
          controller.blankBoardShow(attacker)
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
  override def update(): Unit = print("")
}
