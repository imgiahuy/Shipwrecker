package launcher

//something is missing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine

object Shipwrecker {

  //something is missing

  val controller = new Controller()
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
