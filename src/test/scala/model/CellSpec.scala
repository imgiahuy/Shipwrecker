package model

import model.Cell
import org.scalatest.matchers.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellSpec extends AnyWordSpec with Matchers {
  "A Cell" when {
    "not set to any value" should {
      val emptyCell = Cell(0)
      "have value 0" in {
        emptyCell.value should be(0)
      }
    }
    "set to a specific value" should {
      val nonEmptyCell = Cell(1)
      "the return value" in {
        nonEmptyCell.value should be(1)
      }
    }
  }
}
