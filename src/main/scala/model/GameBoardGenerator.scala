package model

class GameBoardGenerator(private var strategy: Strategy) {

  def setStrategy(strategy: Strategy): Unit = {
    this.strategy = strategy
  }

  def createStrategy(): GameBoard = {
    strategy.create()
  }
}
