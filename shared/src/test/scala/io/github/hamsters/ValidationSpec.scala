package io.github.hamsters

import io.github.hamsters.Validation._
import org.scalatest._

import scala.util.{Failure, Success, Try}

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give no failures" in {
    val e1 = Right(1)
    val e2 = Right(2)
    val e3 = Right(3)

    Validation.failures(e1,e2,e3) should be(Nil)
  }

  "Validation" should "give failures" in {
    val e1 = Right(1)
    val e2 = Left("nan")
    val e3 = Failure(new IllegalArgumentException("nan2"))

    Validation.failures(e1,e2,e3) should be(List("nan", "nan2"))
  }

  "Validation" should "give failures with mixed types" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan")

    Validation.failures(e1, e2, e3) should be(List("nan"))
  }

  "Validation" should "give values if all OK" in {
    val e1 = Right(1)
    val e2 = Right("2")

    Validation.run(e1, e2) should be(Right((1,"2")))
  }

  "Validation mixing Either and Try" should "give values if all OK" in {
    val e1 = Right(1)
    val e2 = Try("2")
    Validation.run(e1, e2) should be(Right((1,"2")))
  }

  "Validation" should "not give values if all are not OK" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan")

    Validation.run(e1, e2, e3) should be(Left(List("nan")))
  }

  "Validation mixing Either and Try" should "not give values if all are not OK" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Failure(new IllegalArgumentException("nan"))

    Validation.run(e1, e2, e3) should be(Left(List("nan")))
  }

  "Validation hasSuccesses of Eithers" should "return true" in {
    val e0 = Left("nan")
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan bis")
    val e4 = Right("3")

    Validation.hasSuccesses(e0, e1, e2, e3, e4)  should be(true)
  }

  "Validation results" should "return only the right values" in {
    val e0 = Left("nan")
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan bis")
    val e4 = Right("3")

    Validation.successes(e0, e1, e2, e3, e4) should have size 3
    Validation.successes(e0, e1, e2, e3, e4) should be(List(1, "2", "3"))
  }

  "Validation hasSuccesses of Trys" should "return true" in {
    Validation.hasSuccesses(
      Failure(new Exception("nan")),
      Success(1),
      Success("2"),
      Failure(new Exception("nan bis")),
      Success("3")
    )  should be(true)
  }

  "Validation results" should "return only the success values" in {
    Validation.successes(
      Failure(new Exception("nan")),
      Success(1),
      Success("2"),
      Failure(new Exception("nan bis")),
      Success("3")
    ) should be(List(1, "2", "3"))
  }



  "OK" should "give a value using get and getOrElse" in {
    val e = Right(1)
    e.get should be(1)
    e.getOrElse(2) should be(1)
  }

  "KO" should "give a value using getOrElse" in {
    val e = Left("d'oh!")
    e.getOrElse(2) should be(2)
  }

  "Either" should "compose using flatMap and map" in {
    val e1 = Right(1)
    val e2 = Right(2)
    val e3 = Right(3)

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield s"$v1-$v2-$v3"

    combine should be(Right("1-2-3"))

  }

  "Either" should "stop at first error" in {

    val e1: Either[String, Int] = Right(1)
    val e2: Either[String, Int] = Left("nan")
    val e3: Either[String, Int] = Left("nan2")

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield s"$v1-$v2-$v3"

    combine should be(Left("nan"))

  }

  "from catchable" should "convert to either" in {

    def compute(x: BigInt) = BigInt(2)/x

    fromCatchable(compute(1)) should be(Right(BigInt(2)))
    fromCatchable(compute(0)) should be(Left("BigInteger divide by zero"))

    fromCatchable(compute(0), (t: Throwable) => t.getClass.getSimpleName) should be(Left("ArithmeticException"))

  }
  "from try" should "convert to either" in {
    fromTry(Try(1)) should be(Right(1))
    fromTry(Failure(new IllegalArgumentException("BigInteger divide by zero"))) should be(Left("BigInteger divide by zero"))
  }
}