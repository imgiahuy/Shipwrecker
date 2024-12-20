package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.GameBoardInterface

class DefaultStrategy extends Strategy {

  override def create(size : Int) : GameBoardInterface = {
    var gameBoard : GameBoardInterface = new GameBoard(size)
    gameBoard
  }
}
