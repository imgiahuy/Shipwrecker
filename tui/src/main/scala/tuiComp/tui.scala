package tuiComp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.Future
import akka.http.scaladsl.model.ContentTypes._

import scala.concurrent.ExecutionContext.Implicits.global

class tui {

  // HTTP Client setup
  implicit val system: ActorSystem = ActorSystem("game-system")

  // Process input from TUI
  def processInput(input: String): Unit = {
    input.trim match {
      case "" => handleEmptyInput()
      case "quit" => handleQuit()
      case command => handleCommand(command)
    }
  }

  def handleEmptyInput(): Unit = println("Input cannot be empty.")

  def handleQuit(): Unit = println("Game ended. Goodbye!")

  def handleCommand(command: String): Unit = {
    command match {
      case "new game" => sendRequest("POST", "/game/new", "")
      case input if input.startsWith("place") => processPlaceShip(input.stripPrefix("place").trim)
      case input if input.startsWith("attack") => processAttack(input.stripPrefix("attack").trim)
      case "undo" => sendRequest("POST", "/game/undo", "")
      case "redo" => sendRequest("POST", "/game/redo", "")
      case "check" => sendRequest("GET", "/game/state", "")
      case "load" => sendRequest("POST", "/game/load", "")
      case "save" => sendRequest("POST", "/game/save", "")
      case _ => println("Unknown command. Please try again.")
    }
  }

  // Send HTTP requests to the server
  private def sendRequest(method: String, path: String, body: String, triggerUpdate: Boolean = true): Unit = {
    val request = method match {
      case "POST" => HttpRequest(HttpMethods.POST, uri = s"http://localhost:8080$path", entity = HttpEntity(ContentTypes.`application/json`, body))
      case "GET"  => HttpRequest(HttpMethods.GET, uri = s"http://localhost:8080$path")
      case _ => throw new IllegalArgumentException("Unsupported HTTP method")
    }

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.onComplete {
      case scala.util.Success(response) =>
        handleResponse(response, triggerUpdate)
      case scala.util.Failure(exception) =>
        println(s"Error: $exception")
    }
  }

  // Handle server responses
  private def handleResponse(response: HttpResponse, triggerUpdate : Boolean): Unit = {
    val status = response.status
    val responseString = Unmarshal(response.entity).to[String]

    responseString.onComplete {
      case scala.util.Success(body) =>
        println(s"Server Response: $status - $body")
        if (status.isSuccess() && triggerUpdate) {
          update()
        }
      case scala.util.Failure(exception) =>
        println(s"Failed to parse response: $exception")
    }
  }

  // --- Observer Update ---
  def update(): Unit = {
    sendRequest("GET", "/game/state", "", triggerUpdate = false)
  }

  // --- Helpers ---
  private def processPlaceShip(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length < 3) {
      println("Invalid format. Use: place <player_name> <x1,y1> <x2,y2> ...")
      println("Example: place Captain 1,1 1,2 1,3")
      return
    }

    val playerName = args(0)
    val positions = args.drop(1)

    val parsedPositions = positions.flatMap { pos =>
      pos.split(",") match {
        case Array(x, y) if x.forall(_.isDigit) && y.forall(_.isDigit) =>
          Some(s"($x,$y)")
        case _ =>
          println(s"Ignored invalid position: $pos")
          None
      }
    }

    if (parsedPositions.isEmpty) {
      println("No valid positions provided.")
      return
    }

    val shipSize = parsedPositions.length
    val body = s"$playerName $shipSize ${parsedPositions.mkString(" ")}"
    sendRequest("POST", "/game/placeShip", body)
  }

  private def processAttack(commandArgs: String): Unit = {
    val args = commandArgs.split(" ")

    if (args.length != 2) {
      println("Invalid input format. Use: attack <attacker_name> <x,y>")
      return
    }

    val attackerName = args(0)
    val coords = args(1).split(",")

    if (coords.length != 2 || !coords(0).forall(_.isDigit) || !coords(1).forall(_.isDigit)) {
      println("Invalid coordinates. Use format: x,y")
      return
    }

    val x = coords(0)
    val y = coords(1)
    val body = s"$attackerName $x,$y"
    sendRequest("POST", "/game/attack", body)
  }
}
