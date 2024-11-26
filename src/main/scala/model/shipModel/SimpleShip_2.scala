package model.shipModel

import model.Ship

class SimpleShip_2(val size: Int = 2) extends Ship{
  override def sizeOf: Int = size
  override def updateShip(): Ship = new SimpleShip_2(size - 1)
  override def isSunk: Boolean = {size <= 0}
  
}
