import org.scalatest.{FlatSpec, Matchers}
import io.github.hamsters.HNil

import scala.collection.mutable.ListBuffer

class HListSpec extends FlatSpec with Matchers {

  "HList cons" should "construct from elements" in {

    ("hi" :: HNil).tail shouldBe HNil
    ("hi" :: HNil).head shouldBe "hi"

    (1 :: "hi" :: HNil).tail shouldBe "hi" :: HNil
    (1 :: "hi" :: HNil).head shouldBe 1

  }

  /*
  "HList ++ " should "append hlist to another one" in {

    ((2.0 :: "hi" :: HNil) ++ (1 :: HNil)) shouldBe 2.0 :: "hi" :: 1 :: HNil
    //type check
    //FIXME val two: Double = ((2.0 :: "hi" :: HNil) ++ (1 :: HNil)).head
    ((2.0 :: "hi" :: HNil) ++ (1 :: HNil)).head shouldBe 2.0

  }*/

  "HList fold" should "old over elements and produce a result" in {

    (2.0 :: "hi" :: HNil).foldLeft("")(_ + _) shouldBe "2.0hi"

  }


  "HList map" should "map elements" in {

    (2.0 :: "hi" :: HNil).map(_.toString) shouldBe ("2.0" :: "hi" :: HNil)

  }

  "HList filter" should "keep only elements satisfying predicate" in {

    //first element : false
    (2.0 :: "hi" :: HNil).filter {
      case s: String if s.startsWith("h") => true
      case _ => false
    } shouldBe ("hi" :: HNil)


    //first element : true
    ("hi" :: 2.0 :: HNil).filter {
      case s: String if s.startsWith("h") => true
      case _ => false
    } shouldBe ("hi" :: HNil)

    //all true
    ("hi" :: "helicopter" :: HNil).filter {
      case s: String if s.startsWith("h") => true
      case _ => false
    } shouldBe ("hi" :: "helicopter" :: HNil)

    //all false
    ("ih" :: 2.0 :: HNil).filter {
      case s: String if s.startsWith("h") => true
      case _ => false
    } shouldBe HNil


  }


  "HList foreach" should "iterate over elements" in {

    val list = ListBuffer[String]()
    (2.0 :: "hi" :: "hello" :: HNil).foreach {
      case s: String => list += s
      case _ =>
    }

    list shouldBe List("hi", "hello")

  }
}
