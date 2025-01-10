package model.GameboardComponent.GameBaseImpl

import com.google.inject.{Inject, Provider}
import model.GameboardComponent.GameBaseImpl.Value.☐


class BoardProvider @Inject() extends Provider[Board[Cell]] {
  override def get(): Board[Cell] = {
    // Provide logic to create a Board instance
    val size = 12  // Example size, could be injected or configured
    val filling: Cell = Cell(☐)   // Provide a default value for Cell
    new Board(size, filling)
  }
}
