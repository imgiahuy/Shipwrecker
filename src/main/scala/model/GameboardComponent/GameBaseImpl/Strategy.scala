package model.GameboardComponent.GameBaseImpl

import model.GameboardComponent.GameBoardInterface

trait Strategy {

  def create(size : Int) : GameBoardInterface

}
