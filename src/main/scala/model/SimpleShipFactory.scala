package model
import model.shipModel.*

class SimpleShipFactory extends ShipFactory {

  override def createShip(size : Int): Ship = {
    if (size == 5) {
      new SimpleShip_5
    } else if(size ==4) {
      new SimpleShip_4
    } else if (size == 3) {
      new SimpleShip_3
    } else if (size == 2) {
      new SimpleShip_2
    } else {
      null
    }
  }
}
