package aview

import controller.ControllerComponent.ControllerInterface
import model.GameboardComponent.GameBaseImpl.State.CONTINUE
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.geometry.Pos.TopLeft
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{BorderPane, GridPane, StackPane, VBox}
import util.Observer

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class gui (controller: ControllerInterface) extends JFXApp3, Observer {

  controller.add(this)

  private val buttonMap: mutable.Map[(Int, Int), Button] = mutable.Map()
  private val buttonMap2: mutable.Map[(Int, Int), Button] = mutable.Map()
  private val buttonMapAttack1: mutable.Map[(Int, Int), Button] = mutable.Map()
  private val buttonMapAttack2: mutable.Map[(Int, Int), Button] = mutable.Map()
  var borderPanel: BorderPane = _
  var messageLabel: Label =_


  private var whoIs : Boolean = true
  private var attackMode : Boolean = false


  override def start(): Unit = {


    val newButtonImage = new Image(getClass.getResource("/base.png").toString)
    val newButtonImageView = new ImageView(newButtonImage)

    val checkButtonImage = new Image(getClass.getResource("/check.png").toString)
    val checkButtonImageView = new ImageView(checkButtonImage)

    val switchButtonImage = new Image(getClass.getResource("/swicht.png").toString)
    val switchButtonImageView = new ImageView(switchButtonImage)

    val backButtonImage = new Image(getClass.getResource("/back.png").toString)
    val backButtonImageView = new ImageView(backButtonImage)

    val startButtonImage = new Image(getClass.getResource("/start.png").toString)
    val startButtonImageView = new ImageView(startButtonImage)

    val attackButtonImage = new Image(getClass.getResource("/attack.png").toString)
    val attackButtonImageView = new ImageView(attackButtonImage)

    val redoButtonImg = new Image(getClass.getResource("/redo.png").toString)
    val redoButtonImageView = new ImageView(redoButtonImg)

    val placeButtonImg = new Image(getClass.getResource("/placebutton.png").toString)
    val placeButtonImageView = new ImageView(placeButtonImg)

    val UndoButtonImg = new Image(getClass.getResource("/undo.png").toString)
    val undoButtonImageView = new ImageView(UndoButtonImg)

    val loadButtonImg = new Image(getClass.getResource("/load.png").toString)
    val loadButtonImageView = new ImageView(loadButtonImg)

    val saveButtonImg = new Image(getClass.getResource("/save1.png").toString)
    val saveButtonImageView = new ImageView(saveButtonImg)


    val captainImage = new Image(getClass.getResource("/captain1.jpg").toString)
    val captainImageView = new ImageView(captainImage)
    captainImageView.fitWidth = 400
    captainImageView.fitHeight = 620

    val pirateImage = new Image(getClass.getResource("/pirate.jpg").toString)
    val pirateImageView = new ImageView(pirateImage)
    pirateImageView.fitWidth = 400
    pirateImageView.fitHeight = 620


    val backgroundImage = new Image(getClass.getResource("/ocean.jpg").toString)
    val backgroundImageView = new ImageView(backgroundImage)
    // Set the background of the Scene using the ImageView
    backgroundImageView.fitWidth = 1200 // Set the width of the image to the stage width
    backgroundImageView.fitHeight = 1000 // Set the height of the image to the stage height

    val coordinate = mutable.ListBuffer[(Int, Int)]()

    var clickCount = 0


    val gridSize = 12
    val buttonSize = 50 //adjust later

    val grid: GridPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(20)
      alignment = TopLeft
    }

    val grid2: GridPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(20)
      alignment = TopLeft
    }

    val gridAttack1: GridPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(20)
      alignment = TopLeft
    }

    val gridAttack2: GridPane = new GridPane {
      hgap = 0
      vgap = 0
      padding = Insets(20)
      alignment = TopLeft
    }


    for (row <- 0 until gridSize; col <- 0 until gridSize) {
      val button: Button = new Button(s"$row,$col") {
        // Ensure the button is a square
        minWidth = buttonSize
        minHeight = buttonSize
        maxWidth = buttonSize
        maxHeight = buttonSize
        style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        var isClicked = false
        // Add click behavior
        onAction = _ =>
          if (isClicked) {
            // Reset the button to its original color when clicked again
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
            coordinate -= ((row, col))
            clickCount -= 1
          } else {
            // Change the color of the button when clicked
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: red;"
            coordinate += ((row, col))
            clickCount += 1
          }
          isClicked = !isClicked // Toggle the state
          println(coordinate)
          println(clickCount)
      }
      grid.add(button, col, row)
      buttonMap((row, col)) = button
    }

    for (row <- 0 until gridSize; col <- 0 until gridSize) {
      val button: Button = new Button(s"$row,$col") {
        // Ensure the button is a square
        minWidth = buttonSize
        minHeight = buttonSize
        maxWidth = buttonSize
        maxHeight = buttonSize
        style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        var isClicked = false
        // Add click behavior
        onAction = _ =>
          if (isClicked) {
            // Reset the button to its original color when clicked again
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
            coordinate -= ((row, col))
            clickCount -= 1
          } else {
            // Change the color of the button when clicked
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: red;"
            coordinate += ((row, col))
            clickCount += 1
          }
          isClicked = !isClicked // Toggle the state
          println(coordinate)
          println(clickCount)
      }
      grid2.add(button, col, row)
      buttonMap2((row, col)) = button
    }

    for (row <- 0 until gridSize; col <- 0 until gridSize) {
      val button: Button = new Button(s"$row,$col") {
        // Ensure the button is a square
        minWidth = buttonSize
        minHeight = buttonSize
        maxWidth = buttonSize
        maxHeight = buttonSize
        style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        var isClicked = false
        // Add click behavior
        onAction = _ =>
          if (isClicked) {
            // Reset the button to its original color when clicked again
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
            coordinate -= ((row, col))
            clickCount -= 1
          } else {
            // Change the color of the button when clicked
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: red;"
            coordinate += ((row, col))
            clickCount += 1
          }
          isClicked = !isClicked // Toggle the state
          println(coordinate)
          println(clickCount)
      }
      gridAttack1.add(button, col, row)
      buttonMapAttack1((row, col)) = button
    }

    for (row <- 0 until gridSize; col <- 0 until gridSize) {
      val button: Button = new Button(s"$row,$col") {
        // Ensure the button is a square
        minWidth = buttonSize
        minHeight = buttonSize
        maxWidth = buttonSize
        maxHeight = buttonSize
        style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        var isClicked = false
        // Add click behavior
        onAction = _ =>
          if (isClicked) {
            // Reset the button to its original color when clicked again
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
            coordinate -= ((row, col))
            clickCount -= 1
          } else {
            // Change the color of the button when clicked
            style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: red;"
            coordinate += ((row, col))
            clickCount += 1
          }
          isClicked = !isClicked // Toggle the state
          println(coordinate)
          println(clickCount)
      }
      gridAttack2.add(button, col, row)
      buttonMapAttack2((row, col)) = button
    }

    messageLabel = new Label("Game Status: Ready") {
      minWidth = 400  // Set a minimum width for the label
      style = """
    -fx-text-fill: darkgreen;  // Set the text color
    -fx-font-size: 18px;  // Set the font size
    -fx-font-family: 'Verdana';  // Set the font family
    -fx-background-color: lightyellow;  // Set the background color
    -fx-padding: 10px;  // Add padding inside the label
    -fx-border-radius: 10px;  // Set rounded corners
    -fx-border-color: darkgreen;  // Border color for the label
    -fx-border-width: 2px;  // Border width
  """
    }

    val placeButton = new Button("Place ship") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = placeButtonImageView
      placeButtonImageView.fitWidth <== width
      placeButtonImageView.fitHeight <== height
      placeButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        if (whoIs) {
          if (coordinate.length <= 5 && coordinate.length >= 2 && controller.getPlayer1.numShip > 0
            && controller.getBoard1.positionValid(coordinate.toList)) {

            controller.placeShips(controller.getPlayer1, coordinate.length, coordinate.toList)
            coordinate.foreach {
              case (row, col) =>
                buttonMap((row, col)).style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: green;"
                buttonMap((row, col)).disable = true
                messageLabel.text = "Ship placed successfully!"
            }
            coordinate.clear()
            println(controller.getPlayer1.numShip)
          }
        } else {
          if (coordinate.length <= 5 && coordinate.length >= 2 && controller.getPlayer2.numShip > 0
            && controller.getBoard2.positionValid(coordinate.toList)) {

            controller.placeShips(controller.getPlayer2, coordinate.length, coordinate.toList)
            coordinate.foreach {
              case (row, col) =>
                buttonMap2((row, col)).style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: green;"
                buttonMap2((row, col)).disable = true
            }
            coordinate.clear()
            println(controller.getPlayer2.numShip)
          }
        }
    }

    val attackButton = new Button("Attack") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = attackButtonImageView
      attackButtonImageView.fitWidth <== width
      attackButtonImageView.fitHeight <== height
      attackButtonImageView.preserveRatio = true

      onAction = _ =>
        if (whoIs) {
          if (coordinate.length == 1) {
            controller.attack(coordinate.head._1, coordinate.head._2, controller.getPlayer2.name)
            if (buttonMap2(coordinate.head._1, coordinate.head._2).isDisable) {
              buttonMapAttack1(coordinate.head._1, coordinate.head._2).style = "-fx-background-color: green;"
              buttonMapAttack1(coordinate.head._1, coordinate.head._2).disable = true
              messageLabel.text = s"Attack at ${coordinate.head._1}, ${coordinate.head._2} was successful!"
            } else {
              buttonMapAttack1(coordinate.head._1, coordinate.head._2).style = "-fx-background-color: red;"
              messageLabel.text = s"Attack at ${coordinate.head._1}, ${coordinate.head._2} was failed!"
            }
            coordinate.clear()
          } else {
            messageLabel.text = " Attack too many location at once !!!"

          }
        } else {
          if (coordinate.length == 1) {
            controller.attack(coordinate.head._1, coordinate.head._2, controller.getPlayer1.name)
            if (buttonMap(coordinate.head._1, coordinate.head._2).isDisable) {
              buttonMapAttack2(coordinate.head._1, coordinate.head._2).style = "-fx-background-color: green;"
              buttonMapAttack2(coordinate.head._1, coordinate.head._2).disable = true
              messageLabel.text = s"Attack at ${coordinate.head._1}, ${coordinate.head._2} was successful!"
            } else {
              buttonMapAttack2(coordinate.head._1, coordinate.head._2).style = "-fx-background-color: red;"
              messageLabel.text = s"Attack at ${coordinate.head._1}, ${coordinate.head._2} was failed!"
            }
            coordinate.clear()
          } else {
            messageLabel.text = " Attack too many location at once !!!"
          }
        }
      style = "-fx-background-color: transparent;"
    }

    val unDoButton = new Button("Undo") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = undoButtonImageView
      undoButtonImageView.fitWidth <== width
      undoButtonImageView.fitHeight <== height
      placeButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _=>
        controller.undo
        refreshGrid()
        messageLabel.text = " Undo was successful!"
    }

    val redoButton = new Button("Redo") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = redoButtonImageView
      redoButtonImageView.fitWidth <== width
      redoButtonImageView.fitHeight <== height
      redoButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        controller.redo
        refreshGrid()
    }

    val switchButton: Button = new Button("Switch") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = switchButtonImageView
      switchButtonImageView.fitWidth <== width
      switchButtonImageView.fitHeight <== height
      switchButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        // Toggle the `whoIs` boolean
        whoIs = !whoIs

        // Dynamically update the left side of the BorderPane
        if (whoIs) {
          borderPanel.left = grid
          borderPanel.right = captainImageView
        } else {
          borderPanel.left = grid2
          borderPanel.right = pirateImageView
        }
         messageLabel.text = s"Grid switched. Current grid: ${if (whoIs) "Player 1" else "Player 2"}"
      }
    }

    val startButton: Button = new Button("Start") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = startButtonImageView
      startButtonImageView.fitWidth <== width
      startButtonImageView.fitHeight <== height
      startButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        if (whoIs) {
          borderPanel.left = gridAttack1
          borderPanel.right = captainImageView
          attackMode = true
          placeButton.disable = true
        } else {
          borderPanel.left = gridAttack2
          borderPanel.right = pirateImageView
          attackMode = true
          placeButton.disable = true
        }
        messageLabel.text = "You are in attack mode !!!"
      }
    }

    val backButton: Button = new Button("Back") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = backButtonImageView
      backButtonImageView.fitWidth <== width
      backButtonImageView.fitHeight <== height
      backButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        if (whoIs) {
          borderPanel.left = grid
          borderPanel.right = captainImageView
          attackMode = false
          placeButton.disable = false
        } else {
          borderPanel.left = grid2
          borderPanel.right = pirateImageView
          attackMode = false
          placeButton.disable = false
        }
        messageLabel.text = "You are in prepare mode !!!"
      }
    }

    val checkButton: Button = new Button("Check") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = checkButtonImageView
      checkButtonImageView.fitWidth <== width
      checkButtonImageView.fitHeight <== height
      checkButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        val check = controller.solver()
        print(controller.getGameState)
    }

    val loadButton: Button = new Button("Load") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = loadButtonImageView
      loadButtonImageView.fitWidth <== width
      loadButtonImageView.fitHeight <== height
      loadButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        controller.load
    }

    val saveButton: Button = new Button("Save") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = saveButtonImageView
      saveButtonImageView.fitWidth <== width
      saveButtonImageView.fitHeight <== height
      saveButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        controller.save
    }

    def cleanGridWithStateReset(grid: GridPane, buttonMap: mutable.Map[(Int, Int), Button]): Unit = {
      for ((key, button) <- buttonMap) {
        button.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        button.disable = false
      }
    }

    val newButton = new Button("new game") {
      minWidth = 150
      minHeight = 50
      maxWidth = 150
      maxHeight = 50
      graphic = newButtonImageView
      newButtonImageView.fitWidth <== width
      newButtonImageView.fitHeight <== height
      newButtonImageView.preserveRatio = true
      style = "-fx-background-color: transparent;"

      onAction = _ =>
        controller.clean()
        cleanGridWithStateReset(grid, buttonMap)
        cleanGridWithStateReset(grid2, buttonMap2)
        cleanGridWithStateReset(gridAttack1, buttonMapAttack1)
        cleanGridWithStateReset(gridAttack2, buttonMapAttack2)
        messageLabel.text = "All grids have been reset!"

    }

    val buttonGrid = new GridPane {
      hgap = 10 // Horizontal gap between buttons
      vgap = 10 // Vertical gap between buttons
      alignment = Pos.TopLeft // Center alignment for the buttons

      add(placeButton, 0, 0)
      add(unDoButton, 1, 0)
      add(redoButton, 0, 1)
      add(attackButton, 1, 1)
      add(switchButton, 2, 0)
      add(startButton, 2, 1)
      add(checkButton,3,0)
      add(backButton,3,1)
      add(newButton,4,0)
      add(loadButton,4,1)
      add(saveButton,5,0)
      add(messageLabel,5,1)
    }

    val functionBox = new VBox {
      spacing = 10
      padding = Insets(20)
      children = Seq(buttonGrid)

      // Style the VBox with a border and background color
      style =
        """
    -fx-border-color: black;
    -fx-border-width: 2px;
    -fx-background-color: lightblue;
    -fx-border-radius: 10px;
    -fx-background-radius: 10px;
  """
    }

    /*Create BorderPanel*/
    borderPanel = new BorderPane {
      left = grid
      bottom = functionBox
      right = captainImageView
    }

    val stackPane = new StackPane {
      children = Seq(backgroundImageView, borderPanel)
    }

    stage = new PrimaryStage {
      title = "HTWG Battleship"
      width = 1200
      height = 1000
      scene = new Scene {
        root = stackPane
      }
    }
  }

  override def update(): Unit = {
    messageLabel.text = controller.getGameState.statement
    controller.adjustGameState(CONTINUE)
  }

  private def refreshGrid(): Unit = {
    // Reset all buttons to default
    if (whoIs) {
      buttonMap.foreach { case ((row, col), button) =>
        button.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        button.disable = false
      }

      buttonMapAttack1.foreach { case ((row, col), button) =>
        button.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        button.disable = false
      }
      // Update the buttons based on the controller state
      val placedShips = controller.getPlacedShips(controller.getPlayer1)
      placedShips.foreach { case (row, col) =>
        val button = buttonMap((row, col))
        button.style = "-fx-background-color: green;" // Highlight placed ship
        button.disable = true // Disable button for placed ship
      }

      val attackShips = controller.getAttackShips(controller.getPlayer1)
      attackShips.foreach { case (row, col) =>
        val button = buttonMapAttack1((row, col))
        button.style = "-fx-background-color: red;" // Mark attack position
        button.disable = true // Disable button after attack
      }

    } else {
      buttonMap2.foreach { case ((row, col), button) =>
        button.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        button.disable = false
      }
      buttonMapAttack2.foreach { case ((row, col), button) =>
        button.style = "-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: transparent;"
        button.disable = false
      }
      // Update the buttons based on the controller state
      val placedShips = controller.getPlacedShips(controller.getPlayer2)
      placedShips.foreach { case (row, col) =>
        val button = buttonMap2((row, col))
        button.style = "-fx-background-color: green;" // Highlight placed ship
        button.disable = true // Disable button for placed ship
      }

      val attackShips = controller.getAttackShips(controller.getPlayer2)
      attackShips.foreach { case (row, col) =>
        val button = buttonMapAttack2((row, col))
        button.style = "-fx-background-color: red;" // Mark attack position
        button.disable = true // Disable button after attack
      }
    }
  }
}
