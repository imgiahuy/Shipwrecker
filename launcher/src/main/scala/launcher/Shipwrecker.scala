package launcher

import FileIOJSON.FileIOJSON
import GameboardComponent.GameBaseImpl.GameBoard
import PlayerComponent.{Player, ShipCounter}
import controllerBaseImpl.Controller
import guiComp.gui
import tuiComp.tui

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine

object Shipwrecker {

  var shipCounter = new ShipCounter(5)
  var board_pl1 = new GameBoard(12)
  var board_pl1_blk = new GameBoard(12)
  var board_pl2 = new GameBoard(12)
  var board_pl2_blk = new GameBoard(12)
  var board_show = new GameBoard(12)
  var player1 = Player("Captain", shipCounter)
  var player2 = Player("Pirate", shipCounter)
  var save = new GameBoard(12)
  var fileIO = new FileIOJSON(save)

  val controller = new Controller(board_pl1, board_pl1_blk,
    board_pl2, board_pl2_blk, board_show,player1, player2, fileIO )
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
      input = readLine()
      Tui.handleCommand(input)
    }
  }
}
