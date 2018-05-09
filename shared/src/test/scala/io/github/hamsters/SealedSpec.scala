package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class SealedSpec extends FlatSpec with Matchers {

  "Sealed.values on a valid sealed trait" should "return a set of sealed trait childs" in {
    sealed trait Colors
    case object Red extends Colors
    case object Orange extends Colors
    case object Green extends Colors

    Sealed.values[Colors] should be equals Set(Red, Orange, Green)
  }

  /**"Sealed.values on a simple trait (non sealed)" should "not compile" in {
    trait Colors
    case object Red extends Colors
    case object Orange extends Colors
    case object Green extends Colors

    Sealed.values[Colors] should be equals Set(Red, Orange, Green) // doesn't compile
  }**/

}
