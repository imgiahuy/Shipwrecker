package model.PlayerComponent

trait PlayerInterface {
  // Gibt den Namen des Spielers zurück
  def name: String
  // Gibt die Anzahl der Schiffe des Spielers zurück
  def numShip: Int

  def shipCounter: ShipCounter // Add this line

  // Verringert die Anzahl der Schiffe des Spielers um 1
  def decrease(): Unit

  // Erhöht die Anzahl der Schiffe des Spielers um 1
  def increase(): Unit

  // Gibt eine String-Repräsentation des Spielers zurück
  override def toString: String
}
