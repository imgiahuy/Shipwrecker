package model.GameboardComponent.GameAdvancedImpl
import model.GameboardComponent.GameBaseImpl.{Strategy, GameBoard as BaseGameBoard}
import model.GameboardComponent.GameBoardInterface

class GameBoard(size : Int) extends BaseGameBoard(size) {
  def createNewGame(size : Int): GameBoardInterface = { (new GameBoardDefaultStrategy).create(size)}
}
class GameBoardDefaultStrategy extends Strategy {

  override def create(size : Int) : GameBoardInterface = {
    var gameBoard: GameBoardInterface = new GameBoard(size)
    gameBoard
  }
}
