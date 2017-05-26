package dl.chatty.some

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

class SimpleTest extends FunSpec with Matchers with GivenWhenThen {
  describe("Scenario") {
    it("Case 1") {
      Given("Nonempty list")
      val list = List(1, 2, 3)
      Then("Size should be greater then 0")
      list.size should be > (0)
    }
  }
}