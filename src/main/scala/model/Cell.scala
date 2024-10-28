package model

case class Cell(value : Value) {
  override def toString: String = value.toString;
}
