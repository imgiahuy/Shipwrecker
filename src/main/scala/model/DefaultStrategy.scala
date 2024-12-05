package model

class DefaultStrategy extends Strategy {

  override def create() : GameBoard = {
    new GameBoard(12)
  }

}
