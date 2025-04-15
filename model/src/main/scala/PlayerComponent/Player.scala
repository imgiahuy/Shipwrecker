package PlayerComponent

case class Player (name: String, shipCounter: ShipCounter) extends PlayerInterface {

  def numShip: Int = shipCounter.value

  override def decrease(): Unit = shipCounter.decrease()

  override def increase(): Unit = shipCounter.increase()

  override def toString: String = name
}
