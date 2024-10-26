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
        print("Who gets to place ships first, " + controller.getNamePlayer1 + " or " + controller.getNamePlayer2 + "?\n")
        val input = readLine()

        val name = if (input == controller.getNamePlayer1) {
          controller.getNamePlayer1
        } else if (input == controller.getNamePlayer2) {
          controller.getNamePlayer2
        } else {
          throw new IllegalArgumentException("Unknown player name")
        }

        var numShip = controller.getNumShip(name)
        controller.showMe()

        while(numShip > 0) {
          var validInput = false // Flag to ensure valid input
          var pox = 0
          var poy = 0
          var richtung = ""

          // Loop until valid ship placement inputs are provided
          while (!validInput) {
            print("\n")
            try {
              print("Place ship at position x: \n")
              pox = readInt()
              print("Place ship at position y: \n")
              poy = readInt()
              print("Horizontal (h) or vertical (v)? \n")
              richtung = readLine().trim.toLowerCase
              // Check for valid input (richtung must be 'h' or 'v')
              if (richtung == "h" || richtung == "v") {
                validInput = controller.placeShips(name, numShip, pox, poy, richtung) // Try to place the ship
                if (!validInput) {
                  print("Unable to place ship, please try again\n")
                }
              } else {
                print("Invalid direction, please enter 'h' for horizontal or 'v' for vertical.\n")
              }
            } catch {
              case _: NumberFormatException =>
                print("Invalid input, please enter a valid number for positions.\n")
            }
          }
          // Show the board after successfully placing the ship
          print("\n")
          controller.boardShow(name)
          numShip = numShip - 1
        }


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
