package model

case class Cell(value : Int) {
  //by default Cell = 0
  //0 if no ship
  //else ship
  override def toString: String = value.toString;
  //replace methode maybe ??
}
