import model.*
import aview.tui
import controller.Controller

import scala.io.StdIn.readLine

object Shipwrecker {
  var board_pl1 = new GameBoard(12)
  var board_pl1_blk = new GameBoard(12)
  var board_pl2 = new GameBoard(12)
  var board_pl2_blk = new GameBoard(12)
  var board_show = new GameBoard(12)
  var player1 =  Player("Huy",5)
  var player2 =  Player("Computer",5)
  val controller = new Controller(board_pl1,board_pl2,board_show,board_pl1_blk,board_pl2_blk,player1,player2)
  val Tui = new tui(controller)

  def main(args: Array[String]): Unit = {
    var input: String = ""
    while (input != "q") {
      println("Game-board : " + board_show.toString)
      input = readLine()
      Tui.processInputLine(input)
    }
  }
}
