package io.github.hamsters


import org.scalatest.{AsyncFlatSpec, Matchers}
import io.github.hamsters.Cartesian._
import io.github.hamsters.Functor._

import scala.concurrent.Future
class CartesianSpec extends AsyncFlatSpec with Matchers  {





  val t1OptionInt: Option[Int] = Some(5)
  val t2SomeInt: Option[Int] = Some(6)
  val t3SomeInt: Option[Int] = Some(7)
  val t4NoneInt: Option[Int] = None


  val t1List = List(6)
  val t2List = List(7)
  val t3List = List(8)

  val t1FutureInt: Future[Int] = Future.successful(66)
  val t2FutureInt: Future[Int] = Future.successful(77)

  "addition on tuple of option" should "be apply when both presents" in {
    (t1OptionInt, t2SomeInt).mapN(_ + _) shouldBe Some(11)
  }

  "addition on tuple of Future Int" should "be apply when both presents" in {
    (t1FutureInt, t2FutureInt).mapN(_ + _) map { _ shouldBe 143 }
  }

  "multiplication on tuple of option" should "be apply when both presents" in {
    (t1OptionInt, t2SomeInt).mapN(_ * _) shouldBe Some(30)
  }

  "addition on tuple of option" should "not be apply when one element is absent" in {
    (t1OptionInt, t4NoneInt).mapN(_ + _) shouldBe None
  }

  "addition on tuple of list" should "be apply if box is List too " in {
    (t1List, t2List).mapN(_ * _) shouldBe List(42)
  }

  "addition on triple of list" should "be apply too" in {
    (t1List, t2List, t3List).mapN( _ + _ + _ ) shouldBe List(21)
  }
}
