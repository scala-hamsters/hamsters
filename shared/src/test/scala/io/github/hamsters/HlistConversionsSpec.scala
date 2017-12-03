package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class HlistConversionsSpec extends FlatSpec with Matchers {

  @HListMacro
  case class Person(name: String, age :Int, weight :Option[Int] = None)

  "Gen on case class" should "generate toClass and toHList" in {
    val p = Person(name = "john", age = 42)

    """Person.toClass("Christophe Colomb"::42::None::HNil) """ should compile

    Person.toClass("Christophe Colomb"::42::None::HNil) shouldEqual Person(name = "Christophe Colomb", age = 42, weight = None)
    Person.toHList(Person(name = "Christophe Colomb", age = 42)) shouldEqual "Christophe Colomb"::42::None::HNil
  }


  "HList" should "convert case class to Hlist and Hlist to case class" in {

    val p = Person(name = "Christophe Colomb", age = 42)

    HList.toHList(p)  shouldEqual "Christophe Colomb"::42::None::HNil
    HList.toClass[Person]("Christophe Colomb"::42::None::HNil)  shouldEqual Person(name = "Christophe Colomb", age = 42)
  }
}
