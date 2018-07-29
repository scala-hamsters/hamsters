package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class EnumSpec extends FlatSpec with Matchers {

  "Enum" should "enumerate and parse an enumerable type" in {

    sealed trait Season
    case object Winter extends Season
    case object Spring extends Season
    case object Summer extends Season
    case object Fall extends Season

    implicit val seasonEnumerable = new Enumerable[Season] {
      override def enumerate: Set[Season] = Sealed.values[Season]
    }

    Enumeration.name(Winter) shouldBe "winter"
    Enumeration.parse("winter") shouldBe Some(Winter)
    //parse must be case-insensitive
    Enumeration.parse("WiNteR") shouldBe Some(Winter)
    Enumeration.parse[Season]("winter") shouldBe Some(Winter)

    Enumeration.enumerate shouldBe Set(Winter, Spring, Summer, Fall)

  }

  "Enum" should "enumerate and parse a custom enumerable type" in {

    sealed trait Season
    case object Winter extends Season
    case object Spring extends Season
    case object Summer extends Season
    case object Fall extends Season

    implicit val seasonEnumerable = new Enumerable[Season] {
      override def enumerate =Sealed.values[Season]

      override def name(s: Season): String  = {
        s match {
          case Winter => "WINTER_SEASON"
          case other => super.name(other)
        }
      }
    }

    Enumeration.name(Winter) shouldBe "WINTER_SEASON"
    Enumeration.parse("WINTER_SEASON") shouldBe Some(Winter)

    Enumeration.enumerate shouldBe Set(Winter, Spring, Summer, Fall)
  }

  "Enum" should "enumerate and parse with several types" in {

    sealed trait Season
    case object Winter extends Season
    case object Spring extends Season
    case object Summer extends Season
    case object Fall extends Season

    sealed trait Color
    case object Red extends Color
    case object Green extends Color
    case object Blue extends Color

    implicit val seasonEnumerable = new Enumerable[Season] {
      override def enumerate: Set[Season] = Set(Winter, Spring, Summer, Fall)
    }

    implicit val colorEnumerable = new Enumerable[Color] {
      override def enumerate: Set[Color] = Sealed.values[Color]
    }

    Enumeration.name(Winter) shouldBe "winter"
    Enumeration.parse[Season]("winter") shouldBe Some(Winter)

    Enumeration.enumerate[Season] shouldBe Set(Winter, Spring, Summer, Fall)
  }


}