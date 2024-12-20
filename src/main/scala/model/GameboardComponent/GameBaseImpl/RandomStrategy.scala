package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.GameBoardInterface
import scala.util.Random

class RandomStrategy extends Strategy {

  override def create(size : Int): GameBoardInterface = {
    var gameBoard: GameBoardInterface = new GameBoard(Random.nextInt(size) + 1)
    gameBoard
    }
  }
