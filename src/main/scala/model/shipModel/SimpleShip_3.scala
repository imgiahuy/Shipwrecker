package model.shipModel

import model.Ship

class SimpleShip_3(val size: Int = 3) extends Ship{
  override def sizeOf: Int = size
  override def updateShip(): Ship = new SimpleShip_3(size - 1)
  override def isSunk: Boolean = {size <= 0}
  
}
