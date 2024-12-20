package controller.ControllerComponent.controllerBaseImpl

import model.GameboardComponent.GameBaseImpl.State
import model.GameboardComponent.GameBaseImpl.State.{CONTINUE, PLAYER_1_WIN, PLAYER_2_WIN}

object GameCase {

  var current: State = CONTINUE

  def handle (current : State) : String = {
    current match {
    case CONTINUE => continueState(current)
    case PLAYER_1_WIN => player1State(current)
    case PLAYER_2_WIN => player2State(current)
    }
  }

  def continueState(current : State) : String = {
    CONTINUE.statement
  }

  def player1State(current : State) : String = {
    PLAYER_1_WIN.statement
  }

  def player2State(current: State) : String = {
    PLAYER_2_WIN.statement
  }

}
