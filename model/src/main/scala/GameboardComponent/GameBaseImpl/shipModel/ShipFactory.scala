package GameboardComponent.GameBaseImpl.shipModel

import GameboardComponent.GameBaseImpl.shipModel.Ship

trait ShipFactory {
  def createShip(size : Int) : Option[ShipInterface]
}
