package model.shipModel

import model.Ship

class SimpleShip_5(val size: Int = 5) extends Ship{
  override def sizeOf: Int = size
  override def updateShip(): Ship = new SimpleShip_4(size - 1)
  override def isSunk: Boolean = {size <= 0}
}
