package ShipwreckerMain


import com.google.inject.name.Names
import com.google.inject.{AbstractModule, TypeLiteral}
import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.controllerBaseImpl.Controller
import model.FileIO.FileIOInterface
import model.FileIO.FileIOXML.FileIOXML
import model.GameboardComponent.GameBaseImpl.{Board, BoardProvider, Cell, GameBoard}
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.{Player, Player2, PlayerInterface}
import net.codingwell.scalaguice.ScalaModule


class ShipwreckerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("ShipNumber1")).to(5)
    bindConstant().annotatedWith(Names.named("ShipNumber2")).to(5)
    bindConstant().annotatedWith(Names.named("Player1")).to("Huy")
    bindConstant().annotatedWith(Names.named("Player2")).to("Computer")
    bind[PlayerInterface].annotatedWith(Names.named("Player1")).to(classOf[Player])
    bind[PlayerInterface].annotatedWith(Names.named("Player2")).to(classOf[Player2])
    bind[PlayerInterface].to(classOf[Player])
    bind[GameBoardInterface].to(classOf[GameBoard])
    bind(new TypeLiteral[Board[Cell]] {}).toProvider(new BoardProvider)
    bind[ControllerInterface].to[controller.ControllerComponent.controllerBaseImpl.Controller]
    bind(classOf[FileIOInterface]).to(classOf[FileIOXML]) // Or to FileIOJSON based on your choice
  }
}
