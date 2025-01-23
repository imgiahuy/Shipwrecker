package model.PlayerComponent

import com.google.inject.Inject
import com.google.inject.name.Named

case class Player @Inject() (@Named("Player1") name: String, @Named("ShipNumber1") var numShip: Int) extends PlayerInterface {
  
  override def decrease(): Unit = numShip = numShip - 1

  override def increase(): Unit = numShip = numShip + 1

  override def toString: String = name
}

