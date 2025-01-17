package model.FileIO.FileIOJSON

import ShipwreckerMain.ShipwreckerModule
import com.google.inject.{Guice, Inject}
import model.FileIO.FileIOInterface
import model.GameboardComponent.GameBaseImpl.Value
import model.GameboardComponent.GameBaseImpl.Value.{X, ☐}
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface
import play.api.libs.json.*

import java.io.*

class FileIOJSON @Inject() (var gameboard: GameBoardInterface) extends FileIOInterface {

  override def load: (GameBoardInterface, GameBoardInterface, GameBoardInterface, GameBoardInterface) = {
    var b1: GameBoardInterface = null
    var b1_blank: GameBoardInterface = null
    var b2: GameBoardInterface = null
    var b2_blank: GameBoardInterface = null

    var player1: PlayerInterface = null
    var player2: PlayerInterface = null

    val file = scala.io.Source.fromFile("game.json").mkString
    val json = Json.parse(file)
    val injector = Guice.createInjector(new ShipwreckerModule)

    // Ship number for player 1
    val shipNum1 = (json \ "gameInfo" \ "ShipNumber1").as[Int]
    player1.numShip = shipNum1

    // Ship number for player 2
    val shipNum2 = (json \ "gameInfo" \ "ShipNumber2").as[Int]
    player2.numShip = shipNum2

    // Load cells for all boards
    loadCells((json \ "board" \ "b1" \ "cell").as[JsArray], b1)
    loadCells((json \ "board" \ "b2" \ "cell").as[JsArray], b2)
    loadCells((json \ "board" \ "b1_blank" \ "cell").as[JsArray], b1_blank)
    loadCells((json \ "board" \ "b2_blank" \ "cell").as[JsArray], b2_blank)

    (b1, b2, b1_blank, b2_blank)
  }

  // Helper function to load cells into a board
  private def loadCells(cells: JsArray, board: GameBoardInterface): Unit = {
    for (cell <- cells.value) {
      val row: Int = (cell \ "row").as[Int]
      val col: Int = (cell \ "col").as[Int]
      val valueText: String = (cell \ "value").as[String]

      val value: Value = valueText match {
        case "X" => X
        case "O" => Value.O
        case "☐" => ☐
      }
      board.updateCell(row, col, value)
    }
  }

  override def save(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit = {
    saveJSON(b1, b2, b1_blank, b2_blank, player1, player2)
  }

  def saveJSON(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit = {
    val json = gameBoardToJSON(b1, b2, b1_blank, b2_blank, player1, player2)
    val jsonString = Json.prettyPrint(json)
    val pw = new PrintWriter(new File("game.json"))
    pw.write(jsonString)
    pw.close()
  }

  def gameBoardToJSON(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): JsValue = {
    Json.obj(
      "gameInfo" -> Json.obj(
        "ShipNumber1" -> player1.numShip,
        "ShipNumber2" -> player2.numShip
      ),
      "board" -> Json.obj(
        "b1" -> Json.obj("cell" -> boardToJSONHelper(b1)),
        "b2" -> Json.obj("cell" -> boardToJSONHelper(b2)),
        "b1_blank" -> Json.obj("cell" -> boardToJSONHelper(b1_blank)),
        "b2_blank" -> Json.obj("cell" -> boardToJSONHelper(b2_blank))
      )
    )
  }

  // Helper method to generate JSON for a single game board
  def boardToJSONHelper(g: GameBoardInterface): JsArray = {
    JsArray(for {
      row <- 0 until g.getCellSize
      col <- 0 until g.getCellSize
    } yield {
      val cellValue = g.getCellValue(row, col) match {
        case Value.X => "X"
        case Value.O => "O"
        case Value.☐ => "☐"
      }
      Json.obj(
        "row" -> row,
        "col" -> col,
        "value" -> cellValue
      )
    })
  }

  override def getPlayer1ShipCount: Int = {
    val file = scala.io.Source.fromFile("game.json").mkString
    val json = Json.parse(file)
    (json \ "gameInfo" \ "ShipNumber1").as[Int]
  }

  override def getPlayer2ShipCount: Int = {
    val file = scala.io.Source.fromFile("game.json").mkString
    val json = Json.parse(file)
    (json \ "gameInfo" \ "ShipNumber2").as[Int]
  }
}
