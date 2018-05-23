package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class ClazzSpec extends FlatSpec with Matchers {

  "Clazz.getSimpleName" should "works on case class" in {

    case class Foo(value : String)

    Clazz.getSimpleName[Foo] shouldBe "Foo"
  }

  "Clazz.getSimpleName" should "works on class" in {

    class Bar

    Clazz.getSimpleName[Bar] shouldBe "Bar"
  }

  "Clazz.getSimpleName" should "works on case object" in {

    case object Quix

    Clazz.getSimpleName[Quix.type ] shouldBe "Quix"
  }


}
