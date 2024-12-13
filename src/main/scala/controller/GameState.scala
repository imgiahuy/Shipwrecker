package controller

object GameState {
  
  type GameState = model.State

  def message(gameStatus: GameState): String = {
    GameCase.handle(gameStatus)
  }
}
