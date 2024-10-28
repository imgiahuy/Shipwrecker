package model

import model.Cell
import model.Value.{O, X, ☐}
import org.scalatest.matchers.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellSpec extends AnyWordSpec with Matchers {
  "A Cell" when {
    "not set to any value" should {
      val emptyCell = Cell(☐)
      "have value ☐" in {
        emptyCell.value should be(☐)
      }
    }
    "set to a specific value" should {
      val nonEmptyCell = Cell(O)
      val nonEmptyCell_1 = Cell(X)
      "the return value" in {
        nonEmptyCell.value should be(O)
        nonEmptyCell_1.value should be(X)
      }
    }
  }
}
