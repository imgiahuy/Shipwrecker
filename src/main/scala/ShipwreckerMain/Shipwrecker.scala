package ShipwreckerMain

import aview.{gui, tui}
import com.google.inject.Guice
import controller.ControllerComponent.ControllerInterface


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine

object Shipwrecker {

  val injector = Guice.createInjector(new ShipwreckerModule)
  val controller = injector.getInstance(classOf[ControllerInterface])
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
