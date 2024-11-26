package model

trait Ship() {
  def sizeOf : Int
  def updateShip() : Ship
  def isSunk : Boolean
}
