package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class UnionSpec extends FlatSpec with Matchers {


  "Union2" should "work with different types" in {
    val stringOrInt = new Union2Type[String, Int]
    import stringOrInt._

    def computeValue(x: Int): Union2[String, Int] = {
      if (x > 0) "0"
      else 1
    }

    computeValue(1) match {
      case Union2(Some(v: String), _) =>
      case _ => fail("wrong union type")
    }

    computeValue(0) match {
      case Union2(_, Some(i: Int)) =>
      case _ => fail("wrong union type")
    }
  }

  "Union3" should "work with different types" in {
    val jsonUnion = new Union3Type[String, Int, Double]
    import jsonUnion._

    //json element can contain a String, a Int or a Double
    def jsonElement(x: Int): Union3[String, Int, Double] = {
      if (x == 0) "0"
      else if (x % 2 == 0) 1
      else 2.0
    }

    jsonElement(0) match {
      case Union3(Some(v: String), _, _) =>
      case _ => fail("wrong union type")
    }

    jsonElement(2) match {
      case Union3(_, Some(v: Int), _) =>
      case _ => fail("wrong union type")
    }

    jsonElement(1) match {
      case Union3(_, _, Some(v: Double)) =>
      case _ => fail("wrong union type")
    }
  }

  "Union2" should "find a type" in {

    val stringOrInt = new Union2Type[String, Int]
    import stringOrInt._

    def computeValue(x: Int): Union2[String, Int] = {
      if (x > 0) "0"
      else 1
    }

    computeValue(0).get[Int] should be(Some(1))

    computeValue(0).get[String] should be(None)

    computeValue(0).getOrElse("default") should be("default")

    //unkown type
    computeValue(0).get[Boolean] should be(None)

    computeValue(1).get[String] should be(Some("0"))

    computeValue(1).get[Int] should be(None)

  }

  "Union2" should "find a type with custom class" in {

    case class Person(name: String)

    val personOrInt = new Union2Type[Person, Int]
    import personOrInt._

    def computeValue(x: Int): Union2[Person, Int] = {
      if (x > 0) Person("bob")
      else 1
    }

    computeValue(0).get[Int] should be(Some(1))

    computeValue(0).get[Person] should be(None)

    computeValue(1).get[Person] should be(Some(Person("bob")))

    computeValue(1).get[Int] should be(None)

  }

  "Union3" should "find a type" in {

    val jsonUnion = new Union3Type[String, Int, Double]
    import jsonUnion._

    //json element can contain a String, a Int or a Double
    def jsonElement(x: Int): Union3[String, Int, Double] = {
      if (x == 0) "0"
      else if (x % 2 == 0) 1
      else 2.0
    }

    jsonElement(0).get[String] should be(Some("0"))

    jsonElement(0).get[Int] should be(None)

    jsonElement(1).get[Double] should be(Some(2.0))

    jsonElement(1).get[String] should be(None)

    jsonElement(2).get[Int] should be(Some(1))

    jsonElement(2).get[String] should be(None)

  }

}