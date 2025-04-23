package controllerBaseImpl

import GameboardComponent.GameBaseImpl.State
import GameboardComponent.GameBaseImpl.State.{CONTINUE, PLAYER_1_WIN, PLAYER_2_WIN}


object GameCase {
  var current: State = CONTINUE
  def handle (current : State) : String = {
    current match {
    case CONTINUE => continueState(current)
    case PLAYER_1_WIN => player1State(current)
    case PLAYER_2_WIN => player2State(current)
    }
  }
  private def continueState(current : State) : String = CONTINUE.statement
  private def player1State(current : State) : String = PLAYER_1_WIN.statement
  private def player2State(current: State) : String = PLAYER_2_WIN.statement
}
