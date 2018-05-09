package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class MonoidSpec extends FlatSpec with Matchers {

  "Monoid empty of a string" should "empty" in {
    Monoid[String].empty shouldBe ""
  }

  "Monoid empty of a List[Int]" should "empty" in {
    Monoid[List[Int]].empty shouldBe List.empty[Int]
  }

}
