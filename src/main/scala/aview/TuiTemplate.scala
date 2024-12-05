package aview

import controller.{Controller, GameState}
import model.State.*
import util.Observer

abstract class TuiTemplate(controller: Controller) extends Observer {
  controller.add(this)

  // Template method defining the skeleton of input processing
  final def processInput(input: String): Unit = {
    input.trim match {
      case "" => handleEmptyInput()
      case "quit" => handleQuit()
      case command => handleCommand(command)
    }
  }

  def handleEmptyInput(): Unit = println("Input cannot be empty.")

  def handleQuit(): Unit = println("Game ended. Goodbye!")

  def handleCommand(command: String): Unit

  final def update(): Unit = {
    displayBoards()
    showGameState()
  }

  // Steps for the update process
  def displayBoards(): Unit = {
    println(s"${controller.getNamePlayer1} Board:")
    controller.boardShow(controller.getNamePlayer1)
    println(s"${controller.getNamePlayer1} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer1)

    println(s"${controller.getNamePlayer2} Board:")
    controller.boardShow(controller.getNamePlayer2)
    println(s"${controller.getNamePlayer2} Blank Board:")
    controller.blankBoardShow(controller.getNamePlayer2)
  }

  def showGameState(): Unit = {
    println("\n")
    GameState.message(controller.gameState)
    controller.gameState = CONTINUE
  }
}

