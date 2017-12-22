package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}
import io.github.hamsters.CartesianProduct._
import TupleBox._
class CartesianProductSpec extends FlatSpec with Matchers  {


  val t1Int: Option[Int] = Some(5)
  val t1Absent: Option[Int] = None

  val t2Int: Option[Int] = Some(6)
  val t3Int: Option[Int] = Some(7)

  val t1List = List(6)
  val t2List = List(7)
  val t3List = List(8)

  "addition on tuple" should "be apply when both presents" in {
    (t1Int, t2Int).mapN(_ + _) shouldBe Some(11)
  }

  "multiplication on tuple" should "be apply when both presents" in {
    (t1Int, t2Int).mapN(_ * _) shouldBe Some(30)
  }

  "addition on tuple" should "not be apply when one element is absent" in {
    (t1Int, t1Absent).mapN(_ + _) shouldBe None
  }

  "addition on tuple" should "be apply if box is List too " in {
    (t1List, t2List).mapN(_ * _) shouldBe List(42)
  }

  "addition on triple" should "be apply too" in {
    (t1List, t2List, t3List).mapN( _ + _ + _ ) shouldBe List(21)
  }
}
