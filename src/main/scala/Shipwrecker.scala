import model.*
import aview.{tui, gui}
import controller.Controller

import scala.io.StdIn.readLine
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Shipwrecker {
  val generator = new GameBoardGenerator(new DefaultStrategy)

  val shipNumber = 5
  var board_pl1 = generator.createStrategy()
  var board_pl1_blk = generator.createStrategy()
  var board_pl2 = generator.createStrategy()
  var board_pl2_blk = generator.createStrategy()
  var board_show = generator.createStrategy()
  var player1 = Player("Huy", shipNumber)
  var player2 = Player("Computer", shipNumber)
  val controller = new Controller(board_pl1, board_pl2, board_show, board_pl1_blk, board_pl2_blk, player1, player2)
  val Tui = new tui(controller)
  val Gui = new gui(controller)

  def main(args: Array[String]): Unit = {
    // Start the GUI in a separate thread
    Future {
      Gui.main(Array()) // Launch the ScalaFX GUI
    }
    // Run the TUI in the main thread
    var input: String = ""
    while (input != "q") {
      println("Game-board : " + board_show.toString)
      input = readLine()
      Tui.handleCommand(input)
    }
  }
}
