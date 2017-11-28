package io.github.hamsters

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success, Try}

class RetrySpec extends FlatSpec with Matchers with MockFactory {

  val logErrorsFunctionMock = mockFunction[String, Unit]

  "Retry" should "run function several times if failed" in {
    logErrorsFunctionMock expects "Tried 3 times, still not enough : failed"
    
    val result = Retry(3, logErrorsFunctionMock) {
      throw new Exception("failed")
    }

    result shouldBe a [Failure[_]]
  }

  "Retry" should "return result if no error" in {
    val result = Retry(3, logErrorsFunctionMock) {
      1+1
    }

    result should be(Success(2))
  }

  "Retry" should "should accept a Successful Try" in {
    val result = Retry.fromTry(3, logErrorsFunctionMock) {
      Try(1+1)
    }

    result should be(Success(2))
  }

  "Retry" should "should accept a Failure Try" in {
    logErrorsFunctionMock expects "Tried 3 times, still not enough : failed"

    val result = Retry.fromTry(3, logErrorsFunctionMock) {
      Failure(new Exception("failed"))
    }

    result shouldBe a [Failure[_]]
  }

}