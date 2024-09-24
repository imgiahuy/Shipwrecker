import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model._

class ShipSpec extends AnyWordSpec with Matchers {

  "A Ship" should {

    "be initialized with the correct size and filling" in {
      val ship = new Ship(3, Cell(1))
      val expectedArray = Array(Cell(1), Cell(1), Cell(1))
      ship.shipCell shouldEqual expectedArray
    }

    "return the correct size" in {
      val ship = new Ship(4, Cell(2))
      ship.sizeOf() shouldEqual 4
    }

    "get the correct cell by index" in {
      val ship = new Ship(5, Cell(3))
      ship.getShipCell(2) shouldEqual Cell(3)
    }

    "return a correctly formatted string representation" in {
      val ship = new Ship(3, Cell(4))
      ship.toString shouldEqual "Array(4, 4, 4)"
    }

    "throw an ArrayIndexOutOfBoundsException when accessing an invalid index" in {
      val ship = new Ship(2, Cell(5))
      an[ArrayIndexOutOfBoundsException] should be thrownBy ship.getShipCell(5)
    }
  }
}
