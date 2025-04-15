package controllerBaseImpl

//something is missing


trait ControllerInterface extends Observable {

  def clean(): Unit

  def placeShips(player: PlayerInterface, shipSize: Int, positions: List[(Int, Int)]): Unit

  def solver(): Unit

  def getNamePlayer1: String

  def getNamePlayer2: String
  
  def getPlayer1 : PlayerInterface
  
  def getPlayer2 : PlayerInterface

  def showMe(): Unit

  def boardShow(player: String): Unit

  def blankBoardShow(player: String): Unit

  def getNumShip(playerName: String): Int

  def attack(pox: Int, poy: Int, player: String): Unit

  def undo: Unit

  def redo: Unit

  def getPlacedShips(player: PlayerInterface): List[(Int, Int)]

  def getAttackShips(player: PlayerInterface): List[(Int, Int)]
  
  def getGameState : GameState
  
  def adjustGameState(state: GameState) : Unit
  
  def getBoard1 : GameBoardInterface
  
  def getBoard2 : GameBoardInterface
  
  def load: Unit
  
  def save: Unit
}