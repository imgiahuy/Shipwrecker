package model

import model.PlayerComponent.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" when {
    "be initialized with name and number of holding ships" should {
      val myPlayer = Player("Huy",5)
      "have expected number of ship equal 5" in {
        myPlayer.numShip shouldEqual (5)
      }
      "have expected name" in {
        myPlayer.name should be("Huy")
      }
    }
    "and will be represent with a toString" should {
      val myPlayer  = Player("Huy", 5)
      "have the expected string format" in {
        myPlayer.toString should be("Huy")
      }
    }
  }
}
