package jvm

import io.github.hamsters.jvm.Retry
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class RetrySpec extends FlatSpec with Matchers with MockFactory {

  val logErrorsFunctionMock = mockFunction[String, Unit]
  
  import scala.concurrent.ExecutionContext.Implicits.global

  "RetryJvm" should "run function several times if failed with wait" in {
    logErrorsFunctionMock expects "Tried 3 times, still not enough : failed"

    val result = Retry.withWait(3, 3000, logErrorsFunctionMock) {
      throw new Exception("failed")
    }

    Await.result(result.failed, Duration.Inf) shouldBe a[Exception]
  }

  "RetryJvm" should "return result if no error with wait" in {
    val result = Retry.withWait(3, 3000, logErrorsFunctionMock) {
      1 + 1
    }

    Await.result(result, Duration.Inf) should be(2)
  }
}