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
  
  val Tui = new tui
  //val Gui = new gui(controller)

  def main(args: Array[String]): Unit = {
    // Start the GUI in a separate thread
    /*Future {
      Gui.main(Array()) // Launch the ScalaFX GUI
    }*/
    // Run the TUI in the main thread
    var input: String = ""
    while (input != "q") {
      input = readLine()
      Tui.handleCommand(input)
    }
  }
}
