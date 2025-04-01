package model.GameboardComponent

import model.PlayerComponent.PlayerInterface
import model.GameboardComponent.GameBaseImpl.{Board, Value}
import model.GameboardComponent.GameBaseImpl.shipModel.ShipInterface

trait GameBoardInterface {
  
  def placeShip(player: PlayerInterface, shipOpt: Option[ShipInterface], positions: List[(Int, Int)], value: CellInterface): GameBoardInterface

  def isPlacementValid(ship: ShipInterface, positions: List[(Int, Int)]): Boolean

  def positionValid(positions: List[(Int, Int)]): Boolean

  def isWithinBounds(x: Int, y: Int): Boolean

  def clean(): Unit

  def hit(where: (Int, Int)): Boolean

  def copyBoard(): GameBoardInterface

  def display(): Unit

  def isEmpty: Boolean

  def getShipPositions: List[(Int, Int)]

  def getAttackPositions: List[(Int, Int)]

  def attack(x: Int, y: Int): Boolean

  def updateCell(x: Int, y: Int, value: Value): Unit

  def getCellValue(x: Int, y: Int): Value
  
  def getCellSize: Int
  
  def createEmptyBoard : GameBoardInterface
  
}

trait CellInterface {}