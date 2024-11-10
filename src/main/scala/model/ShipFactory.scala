package model

trait ShipFactory {
  def createShip(size : Int) : Ship
}
