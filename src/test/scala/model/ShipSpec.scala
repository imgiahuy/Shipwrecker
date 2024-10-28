package model

import model.*
import model.Value.{O, ☐}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ShipSpec extends AnyWordSpec with Matchers {

  "A Ship" should {

    "be initialized with the correct size and filling" in {
      val ship = new Ship(3, Cell(O))
      val expectedArray = Array(Cell(O), Cell(O), Cell(O))
      ship.shipCell shouldEqual expectedArray
    }

    "return the correct size" in {
      val ship = new Ship(4, Cell(O))
      ship.sizeOf() shouldEqual 4
    }

    "get the correct cell by index" in {
      val ship = new Ship(5, Cell(O))
      ship.getShipCell(2) shouldEqual Cell(O)
    }

    "return a correctly formatted string representation" in {
      val ship = new Ship(3, Cell(O))
      ship.toString shouldEqual "Array(O, O, O)"
    }

    "throw an ArrayIndexOutOfBoundsException when accessing an invalid index" in {
      val ship = new Ship(2, Cell(O))
      an[ArrayIndexOutOfBoundsException] should be thrownBy ship.getShipCell(5)
    }
  }
}
