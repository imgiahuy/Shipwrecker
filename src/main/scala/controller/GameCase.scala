package controller

import model.State
import model.State.{CONTINUE, PLAYER_1_WIN, PLAYER_2_WIN}

object GameCase {

  var current: State = CONTINUE

  def handle (current : State) = {
    current match {
    case CONTINUE => continueState(current)
    case PLAYER_1_WIN => player1State(current)
    case PLAYER_2_WIN => player2State(current)
    }
  }

  def continueState(current : State) = {
    println(CONTINUE.statement)
  }

  def player1State(current : State) = {
    println(PLAYER_1_WIN.statement)
  }

  def player2State(current: State) = {
    println(PLAYER_2_WIN.statement)
  }

}
