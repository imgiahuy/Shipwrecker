package controller.ControllerComponent

import controller.ControllerComponent.controllerBaseImpl.GameCase
import model.GameboardComponent.GameBaseImpl.State

object GameState {
  
  type GameState = State

  def message(gameStatus: GameState): String = {
    GameCase.handle(gameStatus)
  }
}
