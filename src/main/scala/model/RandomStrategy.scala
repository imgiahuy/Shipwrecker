package model

import scala.util.Random

class RandomStrategy extends Strategy {

  override def create(): GameBoard = {
    new GameBoard(Random.nextInt(100) + 1)
  }

}
