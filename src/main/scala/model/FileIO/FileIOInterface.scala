package model.FileIO

import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface

trait FileIOInterface {

  def load: (GameBoardInterface, GameBoardInterface, GameBoardInterface, GameBoardInterface, Int, Int)
  def save(b1: GameBoardInterface, b2 : GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit
  def getPlayer1ShipCount: Int
  def getPlayer2ShipCount: Int
  
}
