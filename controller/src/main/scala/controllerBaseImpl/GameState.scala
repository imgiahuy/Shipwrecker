package controllerBaseImpl

import GameboardComponent.GameBaseImpl.State

//something is missing


object GameState {
  
  type GameState = State

  def message(gameStatus: GameState): String = {
    GameCase.handle(gameStatus)
  }
}
