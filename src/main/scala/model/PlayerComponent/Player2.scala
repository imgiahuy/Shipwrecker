package model.PlayerComponent

import com.google.inject.Inject
import com.google.inject.name.Named

case class Player2 @Inject() (@Named("Player2") name: String, @Named("ShipNumber2") shipCounter: ShipCounter) extends PlayerInterface {

  def numShip: Int = shipCounter.value

  override def decrease(): Unit = shipCounter.decrease()

  override def increase(): Unit = shipCounter.increase()

  override def toString: String = name
}

