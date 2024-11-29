package model

case class Player(name: String, var numShip: Int) {
  def decrease(): Unit = numShip = numShip - 1

  def increase(): Unit = numShip = numShip + 1

  override def toString: String = name
}

