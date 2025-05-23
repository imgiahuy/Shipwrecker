package GameboardComponent.GameBaseImpl.shipModel

import GameboardComponent.GameBaseImpl.shipModel.*

class SimpleShipFactory extends ShipFactory {

  override def createShip(size : Int): Option[ShipInterface] = {
    size match {
      case 5 => Some(new SimpleShip_5)
      case 4 => Some(new SimpleShip_4)
      case 3 => Some(new SimpleShip_3)
      case 2 => Some(new SimpleShip_2)
      case _ => None // Explicitly represent absence
    }
  }
}
