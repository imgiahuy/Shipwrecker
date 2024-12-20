package model.GameboardComponent.GameBaseImpl

enum State (val statement : String) {
  case CONTINUE extends State("Continue to play the game")
  case PLAYER_1_WIN extends State("Player 1 has won the game")
  case PLAYER_2_WIN extends State("Player 2 has won the game")
}