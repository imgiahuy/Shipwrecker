package rest

import FileIOJSON.FileIOJSON
import GameboardComponent.GameBaseImpl.GameBoard
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import controllerBaseImpl.{Controller, GameState}
import PlayerComponent.{Player, ShipCounter}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}

import scala.concurrent.{ExecutionContext, Future}

object GameRestApi {
  implicit val system: ActorSystem = ActorSystem("game-system")
  implicit val executionContext: ExecutionContext = system.dispatcher

  var shipCounter = ShipCounter(5)
  var board_pl1 = new GameBoard(12)
  var board_pl1_blk = new GameBoard(12)
  var board_pl2 = new GameBoard(12)
  var board_pl2_blk = new GameBoard(12)
  var board_show = new GameBoard(12)
  var player1 = Player("Captain", shipCounter)
  var player2 = Player("Pirate", shipCounter)
  var save = new GameBoard(12)
  var fileIO = new FileIOJSON(save)

  val controller: Controller = new Controller(board_pl1, board_pl1_blk, board_pl2, board_pl2_blk, board_show, player1, player2, fileIO)

  val route: Route =
    pathPrefix("game") { // Ensure that this pathPrefix is properly placed
      path("placeShip") {
        post {
          entity(as[String]) { data =>
            val parts = data.split(" ")
            if (parts.length >= 3) {
              val playerName = parts(0)
              val shipSize = parts(1).toInt
              val positions = parts.drop(2).map { pos =>
                val coords = pos.stripPrefix("(").stripSuffix(")").split(",")
                (coords(0).toInt, coords(1).toInt)
              }.toList

              val maybePlayer = playerName match {
                case name if name == controller.getNamePlayer1 => Some(controller.getPlayer1)
                case name if name == controller.getNamePlayer2 => Some(controller.getPlayer2)
                case _ => None
              }

              maybePlayer match {
                case Some(player) =>
                  controller.placeShips(player, shipSize, positions)
                  complete(s"Ship placed for $playerName.")
                case None =>
                  complete(s"Unknown player: $playerName")
              }
            } else {
              complete("Invalid input.")
            }
          }
        }
      } ~
        path("attack") {
          post {
            entity(as[String]) { data =>
              val parts = data.split(" ")
              if (parts.length == 2) {
                val playerName = parts(0)
                val coords = parts(1).split(",")

                if (coords.length == 2 && coords(0).forall(_.isDigit) && coords(1).forall(_.isDigit)) {
                  val x = coords(0).toInt
                  val y = coords(1).toInt
                  controller.attack(x, y, playerName)
                  controller.update()
                  complete(s"$playerName attacked at ($x, $y).")
                } else {
                  complete("Invalid coordinate format. Use x,y with numbers.")
                }
              } else {
                complete("Invalid attack input. Use format: <player_name> x,y")
              }
            }
          }
        } ~
      path("undo") {
          post {
            controller.undo
            complete("Undo successful.")
          }
        } ~
        path("redo") {
          post {
            controller.redo
            complete("Redo successful.")
          }
        } ~
        path("save") {
          post {
            controller.save
            complete("Game saved.")
          }
        } ~
        path("load") {
          post {
            controller.load
            complete("Game loaded.")
          }
        } ~
        path("new") {
          post {
            controller.clean()
            complete("New Game.")
          }
        } ~
        path("state") {
          get {
            val stateString = GameState.message(controller.getGameState)
            val boardView =
              s"""|
                  |${controller.getNamePlayer1} Board:
                  |${controller.boardShow(controller.getNamePlayer1)}
                  |
                  |${controller.getNamePlayer1} Blank Board:
                  |${controller.blankBoardShow(controller.getNamePlayer1)}
                  |
                  |${controller.getNamePlayer2} Board:
                  |${controller.boardShow(controller.getNamePlayer2)}
                  |
                  |${controller.getNamePlayer2} Blank Board:
                  |${controller.blankBoardShow(controller.getNamePlayer2)}
                  |""".stripMargin

            println("Returning game state and boards:")
            println(stateString + "\n\n" + boardView)

            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, stateString + "\n\n" + boardView))
          }
        }
    }

  private def startServer(): Future[Http.ServerBinding] = {
    Http().newServerAt("localhost", 8080).bind(route)
  }

  def main(args: Array[String]): Unit = {
    startServer().onComplete {
      case scala.util.Success(binding) =>
        println(s"Server started at ${binding.localAddress}")
      case scala.util.Failure(ex) =>
        println(s"Failed to bind HTTP server: $ex")
        system.terminate()
    }
  }
}
