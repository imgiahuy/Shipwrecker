package aview
import model.*

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.*

class tui {

  def processInputLine(input: String, b1: GameBoard, b2: GameBoard, show : GameBoard, b1_blank: GameBoard, b2_blank: GameBoard,
                       player1: Player, player2: Player ): GameBoard = {
    input match {
      case "new game" =>
        var i = 0
        b1.clean()
        b2.clean()
        print("Player " + player1.name + "'s board: ...\n")
        b1.display()
        print("Player " + player2.name + "'s board: ...\n")
        b2.display()
        show
      case "quit" => show
      case "place ship player 1" =>
        var numShipHolder = player1.numShip
        while( 0 < numShipHolder ) {
          print(player1.name + " place ship...\n")
          print("ship size: " + numShipHolder +"\n")
          b1.display()
          print("x postion: \n")
          val pox = readInt()
          print("y postion: \n")
          val poy = readInt()
          print("which direction:\n")
          val richtung = readLine()
          b1.placeShip(new Ship(numShipHolder,Cell(numShipHolder)),(pox,poy),richtung)
          b1.display()
          numShipHolder = numShipHolder - 1
        }
        show
      case "place ship player 2" =>
        var numShipHolder2 = player2.numShip
        while( 0 < numShipHolder2 ) {
          print(player2.name + " place ship...\n")
          print("ship size: " + numShipHolder2 + "\n")
          b2.display()
          print("x postion: \n")
          val pox = readInt()
          print("y postion: \n")
          val poy = readInt()
          print("which direction:\n")
          val richtung = readLine()
          b2.placeShip(new Ship(numShipHolder2,Cell(numShipHolder2)),(pox,poy),richtung)
          b2.display()
          numShipHolder2 = numShipHolder2 - 1
        }
        show
      case "" =>
        println("Input cannot be empty.") // handle empty input
        show
      case "attack player 2" =>
        show.display()
        print("attack x postion: \n")
        val pox = readInt()
        print("attack y postion: \n")
        val poy = readInt()
        val hit = b2.hit(pox,poy)
        if (hit) {
          print("hit successful at " + pox + ", " + poy + "\n")
          b1_blank.cells.replace(pox, poy, Cell(1))
          b1_blank.display()
        } else {
          print("hit failed at " + pox + ", " + poy + "\n")
          b1_blank.cells.replace(pox, poy, Cell(9)) //for now 9 is a failed hit
          b1_blank.display()
        }
        show
      case "attack player 1" =>
        show.display()
        print("attack x postion: \n")
        val pox = readInt()
        print("attack y postion: \n")
        val poy = readInt()
        val hit = b1.hit(pox,poy)
        if (hit) {
          print("hit successful at " + pox + ", " + poy + "\n")
          b2_blank.cells.replace(pox, poy, Cell(1))
          b2_blank.display()
        } else {
          print("hit failed at " + pox + ", " + poy + "\n")
          b2_blank.cells.replace(pox, poy, Cell(9)) //for now 9 is a failed hit
          b2_blank.display()
        }
        show
      case "check" =>
        val solver =  Solver()
        if (solver.solved(b1_blank.copyBoard(), b2.copyBoard())) {
          print("Game finish, player " + player1.name + " has won")
          b2.display()
        }
        else if (solver.solved(b2_blank.copyBoard(), b1.copyBoard())) {
          print("Game finish, player " + player2.name + " has won")
          b1.display()
        }
        else{
          print("Game not finish")
        }
        show
      case "test only" =>
        b1_blank.copyBoard().display()
        print("\n")
        b2.copyBoard().display()
        print("\n")
        b2_blank.copyBoard().display()
        print("\n")
        b1.copyBoard().display()
        show
    }
  }
}
