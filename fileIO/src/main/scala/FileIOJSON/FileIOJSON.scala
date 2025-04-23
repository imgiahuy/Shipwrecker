package FileIOJSON

import GameboardComponent.GameBaseImpl.Value
import GameboardComponent.GameBaseImpl.Value.*
import GameboardComponent.GameBoardInterface
import PlayerComponent.PlayerInterface
import GameboardComponent.GameBaseImpl.Value
import GameboardComponent.GameBoardInterface
import PlayerComponent.PlayerInterface
import play.api.libs.json.*

import java.io.*


class FileIOJSON  (val gameboard: GameBoardInterface) extends FileIOInterface {

  override def load: (GameBoardInterface, GameBoardInterface, GameBoardInterface, GameBoardInterface, Int, Int) = {

    val b1: GameBoardInterface = gameboard.createEmptyBoard
    val b1_blank: GameBoardInterface = gameboard.createEmptyBoard
    val b2: GameBoardInterface = gameboard.createEmptyBoard
    val b2_blank: GameBoardInterface = gameboard.createEmptyBoard

    val player1: Int = getPlayer1ShipCount
    val player2: Int = getPlayer2ShipCount

    val file = scala.io.Source.fromFile("game.json").mkString
    val json = Json.parse(file)

    // Load cells for all boards
    loadCells((json \ "board" \ "b1" \ "cell").as[JsArray], b1)
    loadCells((json \ "board" \ "b2" \ "cell").as[JsArray], b2)
    loadCells((json \ "board" \ "b1_blank" \ "cell").as[JsArray], b1_blank)
    loadCells((json \ "board" \ "b2_blank" \ "cell").as[JsArray], b2_blank)

    (b1, b2, b1_blank, b2_blank, player1, player2)
  }

  // Helper function to load cells into a board
  private def loadCells(cells: JsArray, board: GameBoardInterface): Unit = {
    for (cell <- cells.value) {
      val row: Int = (cell \ "row").as[Int]
      val col: Int = (cell \ "col").as[Int]
      val valueText: String = (cell \ "value").as[String]

      val value: Value = valueText match {
        case "X" => Value.X
        case "O" => Value.O
        case "☐" => Value.☐
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
