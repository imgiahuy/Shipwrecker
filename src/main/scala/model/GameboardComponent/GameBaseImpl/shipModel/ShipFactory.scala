package model.GameboardComponent.GameBaseImpl.shipModel

import model.GameboardComponent.GameBaseImpl.shipModel.Ship

trait ShipFactory {
  def createShip(size : Int) : Option[ShipInterface]
}
