package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class NonEmptyListSpec extends FlatSpec with Matchers {

  "NonEmptyList" should "handle a single parameter" in {
    NonEmptyList(42).head should be(42)
  }

  "NonEmptyList" should "accept a List as a second parameter and return it as its tail" in {
    NonEmptyList(0, List(1, 2)).tail should be(List(1, 2))
  }

  "NonEmptyList" should "accept a variable number of parameters and return the 2nd and above parameters as its tail" in {
    NonEmptyList(3, 4, 5).tail should be(List(4, 5))
  }

}
