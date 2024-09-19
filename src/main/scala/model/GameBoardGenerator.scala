package model

case class GameBoardGenerator(size : Int) {
  def boardDefault () : GameBoard = new GameBoard(12)
  def boardCustom () : GameBoard = new GameBoard(size)
}
