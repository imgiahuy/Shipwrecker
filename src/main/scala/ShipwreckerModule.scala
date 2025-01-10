import com.google.inject.{AbstractModule, TypeLiteral}
import com.google.inject.name.Names
import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.controllerBaseImpl.Controller
import model.GameboardComponent.GameBaseImpl.{Board, BoardProvider, Cell, GameBoard}
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.{Player, Player2, PlayerInterface}
import net.codingwell.scalaguice.ScalaModule


class ShipwreckerModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("ShipNumber")).to(5)
    bindConstant().annotatedWith(Names.named("Player1")).to("Huy")
    bindConstant().annotatedWith(Names.named("Player2")).to("Computer") // Example constant for numShip
    bind[GameBoardInterface].to[GameBoard]
    bind[PlayerInterface].annotatedWith(Names.named("Player1")).to[Player]
    bind[PlayerInterface].annotatedWith(Names.named("Player2")).to[Player2]
    bind(new TypeLiteral[Board[Cell]] {}).toProvider(new BoardProvider)
    bind[ControllerInterface].to[controller.ControllerComponent.controllerBaseImpl.Controller]
  }
}
