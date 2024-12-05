package controller

object GameState {
  
  type GameState = model.State

  def message(gameStatus: GameState): Unit = {
    GameCase.handle(gameStatus)
  }
}
