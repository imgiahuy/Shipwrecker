package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.CellInterface

case class Cell(value : Value) extends CellInterface {
  override def toString: String = value.toString;
}
