package model.PlayerComponent

import com.google.inject.Inject
import com.google.inject.name.Named

case class Player2 @Inject() (@Named("Player2") name: String, @Named("ShipNumber2") var numShip: Int) extends PlayerInterface {

  override def decrease(): Unit = numShip = numShip - 1

  override def increase(): Unit = numShip = numShip + 1

  override def toString: String = name
}

