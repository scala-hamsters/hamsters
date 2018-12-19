package io.github.hamsters

import io.github.hamsters.EitherOps._
import org.scalatest._

import scala.util.{Failure, Success, Try}

class EitherOpsSpec extends FlatSpec with Matchers {

  "EitherOps" should "give no lefts" in {
    val e1 = Right(1)
    val e2 = Right(2)
    val e3 = Right(3)

    EitherOps.collectLefts(e1,e2,e3) should be(Nil)
  }

  "EitherOps" should "give lefts" in {
    val e1 = Right(1)
    val e2 = Left("nan")
    val e3 = Left("nan2")

    EitherOps.collectLefts(e1,e2,e3) should be(Seq("nan", "nan2"))
  }

  "EitherOps" should "give lefts with mixed right types" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan")

    EitherOps.collectLefts(e1, e2, e3) should be(Seq("nan"))
  }

  "EitherOps" should "give lefts with mixed error types" in {
    
    sealed trait ValidationError
    case class NumericError(message: String) extends ValidationError
    case class OtherError(message: String) extends ValidationError

    val e1 = Right(1)
    val e2 = Left(NumericError("nan"))
    val e3 = Left(OtherError("foo"))

    val lefts: Seq[ValidationError] = EitherOps.collectLefts(e1,e2,e3)
    lefts should be(Seq(NumericError("nan"), OtherError("foo")))
  }

  "collectRights" should "return only the right values" in {
    val e0 = Left("nan")
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan bis")
    val e4 = Right("3")

    EitherOps.collectRights(e0, e1, e2, e3, e4) should have size 3
    EitherOps.collectRights(e0, e1, e2, e3, e4) should be(Seq(1, "2", "3"))
  }   

  "from catchable" should "convert to either" in {
    def compute(x: BigInt) = BigInt(2)/x

    fromCatchable(compute(1)) should be(Right(BigInt(2)))
    fromCatchable(compute(0)) should be(Left("BigInteger divide by zero"))

    fromCatchable(compute(0), (t: Throwable) => t.getClass.getSimpleName) should be(Left("ArithmeticException"))
  }
}