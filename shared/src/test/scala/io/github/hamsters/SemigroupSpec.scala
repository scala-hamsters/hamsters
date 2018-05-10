package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class SemigroupSpec extends FlatSpec with Matchers {

  "Semigroup empty of a string" should "empty" in {
    Semigroup[Int].combine(1,2) shouldBe 3
  }

  "Monoid empty of a List[Int]" should "empty" in {
    Semigroup[List[String]].combine(List("1"), List("2")) shouldBe List("1", "2")
  }

}
