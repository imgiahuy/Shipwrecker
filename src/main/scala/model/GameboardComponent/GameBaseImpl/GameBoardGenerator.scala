package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.GameBoardInterface


class GameBoardGenerator(private var strategy: Strategy) {

  def setStrategy(strategy: Strategy): Unit = {
    this.strategy = strategy
  }

  def createStrategy(size : Int): GameBoardInterface = {
    strategy.create(size)
  }
}
