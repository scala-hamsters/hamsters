package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class EmptyOptionValuesSpec extends FlatSpec with Matchers {

  import io.github.hamsters.EmptyOptionValues._

  "A none element of Option[String]" should "return empty string" in {
    val optString: Option[String] = None
    optString.orEmpty shouldEqual ""
  }

  "An none element of Option[Iterable] of multiple data types" should "return empty iterable" in {
    val optListString: Option[List[String]] = None
    optListString.orEmpty shouldEqual Iterable[String]()

    val optListInt: Option[List[Int]] = None
    optListInt.orEmpty shouldEqual Iterable[Int]()

    val optListMyCaseClass: Option[List[MyCaseClass]] = None
    optListMyCaseClass.orEmpty shouldEqual Iterable[MyCaseClass]()
  }

  "A none element of Option[Int]" should "return 0" in {
    val optInt: Option[Int] = None
    optInt.orEmpty shouldEqual 0
  }

  "A none element of Option[Float]" should "return 0f" in {
    val optFloat: Option[Float] = None
    implicit val monoidFloat: Monoid[Float] = Monoid.floatMonoid
    optFloat.orEmpty shouldEqual 0f
  }

  "A none element of Option[Double]" should "return 0d" in {
    val optDouble: Option[Double] = None
    implicit val monoidDouble: Monoid[Double] = Monoid.doubleMonoid
    optDouble.orEmpty shouldEqual 0d
  }

  "A none element of Option[BigDecimal]" should "return 0" in {
    val optBigDecimal: Option[BigDecimal] = None
    optBigDecimal.orEmpty shouldEqual 0
  }
}
