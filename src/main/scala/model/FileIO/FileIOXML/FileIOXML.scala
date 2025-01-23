package model.FileIO.FileIOXML

import ShipwreckerMain.ShipwreckerModule
import com.google.inject.{Guice, Inject}
import model.FileIO.FileIOInterface
import model.GameboardComponent.GameBaseImpl.Value
import model.GameboardComponent.GameBaseImpl.Value.{X, ☐}
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.PlayerInterface

import scala.xml.{Elem, NodeSeq, PrettyPrinter}

class FileIOXML @Inject() (var gameboard: GameBoardInterface)  extends FileIOInterface {

  override def load: (GameBoardInterface, GameBoardInterface, GameBoardInterface, GameBoardInterface) = {

    val file = scala.xml.XML.loadFile("game.xml")
    val injector = Guice.createInjector(new ShipwreckerModule)

    val b1: GameBoardInterface = injector.getInstance(classOf[GameBoardInterface])
    val b1_blank: GameBoardInterface = injector.getInstance(classOf[GameBoardInterface])
    val b2: GameBoardInterface = injector.getInstance(classOf[GameBoardInterface])
    val b2_blank: GameBoardInterface = injector.getInstance(classOf[GameBoardInterface])

    val player1: PlayerInterface = injector.getInstance(classOf[PlayerInterface])
    val player2: PlayerInterface = injector.getInstance(classOf[PlayerInterface])

    // Ship number for player 1
    val shipAttr1 = file \\ "gameInfo" \ "@ShipNumber1"
    val shipNum1 = shipAttr1.text.toInt
    player1.numShip = shipNum1

    // Ship number for player 2
    val shipAttr2 = file \\ "gameInfo" \ "@ShipNumber2"
    val shipNum2 = shipAttr2.text.toInt
    player2.numShip = shipNum2

    // Load cells for all boards
    val cell_b1 = file \\ "board" \ "b1" \\ "cell"
    val cell_b2 = file \\ "board" \ "b2" \\ "cell"
    val cell_b1_blank = file \\ "board" \ "b1_blank" \\ "cell"
    val cell_b2_blank = file \\ "board" \ "b2_blank" \\ "cell"

    // Fill the cells for each board
    loadCells(cell_b1, b1)
    loadCells(cell_b2, b2)
    loadCells(cell_b1_blank, b1_blank)
    loadCells(cell_b2_blank, b2_blank)

    (b1, b2, b1_blank, b2_blank)
  }

  // Helper function to load cells into a board
  private def loadCells(cells: NodeSeq, board: GameBoardInterface): Unit = {
    for (cell <- cells) {
      val rowStr = (cell \ "@row").text
      val colStr = (cell \ "@col").text
      val valueText = cell.text.trim

      val row = rowStr.toInt
      val col = colStr.toInt
      val value: Value = valueText match {
        case "X" => X
        case "O" => Value.O
        case "☐" => ☐
        case _ => ☐ // Default-Wert, falls Wert unbekannt ist
      }

      board.updateCell(row, col, value)
    }
  }

  override def save(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit = {
    saveString(b1, b2, b1_blank, b2_blank, player1, player2)
  }

  def saveXML(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit = {
    scala.xml.XML.save("game.xml", gameBoardToXML(b1, b2, b1_blank, b2_blank, player1, player2))
  }

  def saveString(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("game.xml"))

    // Generiere das XML für jedes der Boards und die Spielerinformationen
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameBoardToXML(b1, b2, b1_blank, b2_blank, player1, player2))

    // Schreibe das XML in die Datei
    pw.write(xml)
    pw.close()
  }

  def gameBoardToXML(b1: GameBoardInterface, b2: GameBoardInterface, b1_blank: GameBoardInterface, b2_blank: GameBoardInterface, player1: PlayerInterface, player2: PlayerInterface): Elem = {
    <game>
      <gameInfo ShipNumber1={player1.numShip.toString} ShipNumber2={player2.numShip.toString}/>
      <board name="b1">
        {gameBoardToXMLHelper(b1)}
      </board>
      <board name="b2">
        {gameBoardToXMLHelper(b2)}
      </board>
      <board name="b1_blank">
        {gameBoardToXMLHelper(b1_blank)}
      </board>
      <board name="b2_blank">
        {gameBoardToXMLHelper(b2_blank)}
      </board>
    </game>
  }

  // Helper method to generate XML for a single game board
  def gameBoardToXMLHelper(g: GameBoardInterface): Seq[scala.xml.Node] = {
    for {
      row <- 0 until g.getCellSize
      col <- 0 until g.getCellSize
    } yield {
      val cellValue = g.getCellValue(row, col) match {
        case Value.X => "X"
        case Value.O => "O"
        case Value.☐ => "☐"
      }
      <cell row={row.toString} col={col.toString}>
        {cellValue}
      </cell>
    }
  }

  override def getPlayer1ShipCount: Int = {
    val file = scala.xml.XML.loadFile("game.xml")
    (file \\ "gameInfo" \ "@ShipNumber1").text.toInt
  }

  override def getPlayer2ShipCount: Int = {
    val file = scala.xml.XML.loadFile("game.xml")
    (file \\ "gameInfo" \ "@ShipNumber2").text.toInt
  }
}
