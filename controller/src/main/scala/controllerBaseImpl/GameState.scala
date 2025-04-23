package controllerBaseImpl

import GameboardComponent.GameBaseImpl.State

import GameboardComponent.GameBaseImpl.State

object GameState {
  
  type GameState = State

  def message(gameStatus: GameState): String = {
    GameCase.handle(gameStatus)
  }
}
