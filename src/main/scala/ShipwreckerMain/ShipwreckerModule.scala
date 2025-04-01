package ShipwreckerMain


import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Provides, Singleton, TypeLiteral}
import controller.ControllerComponent.ControllerInterface
import controller.ControllerComponent.controllerBaseImpl.Controller
import model.FileIO.FileIOInterface
import model.FileIO.FileIOJSON.FileIOJSON
import model.GameboardComponent.GameBaseImpl.{Board, BoardProvider, Cell, GameBoard}
import model.GameboardComponent.GameBoardInterface
import model.PlayerComponent.{Player, Player2, PlayerInterface, ShipCounter}
import net.codingwell.scalaguice.ScalaModule

class ShipwreckerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("Player1")).to("Huy")
    bindConstant().annotatedWith(Names.named("Player2")).to("Computer")

    // Bind Player instances
    bind[PlayerInterface].annotatedWith(Names.named("Player1")).to(classOf[Player])
    bind[PlayerInterface].annotatedWith(Names.named("Player2")).to(classOf[Player2])
    bind[PlayerInterface].to(classOf[Player])

    // Bind game-related instances
    bind[GameBoardInterface].to(classOf[GameBoard])
    bind(new TypeLiteral[Board[Cell]] {}).toProvider(new BoardProvider)
    bind[ControllerInterface].to[Controller]
    bind(classOf[FileIOInterface]).to(classOf[FileIOJSON])
  }

  // Provide ShipCounter instances correctly
  @Provides
  @Named("ShipNumber1")
  def provideShipCounter1(): ShipCounter = ShipCounter(5)

  @Provides
  @Named("ShipNumber2")
  def provideShipCounter2(): ShipCounter = ShipCounter(5)
}

