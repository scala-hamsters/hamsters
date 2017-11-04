import io.github.hamsters.Retry
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.util.{Failure, Success, Try}
import org.scalamock.scalatest.MockFactory

class RetrySpec extends AsyncFlatSpec with Matchers with MockFactory {

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

  "Retry" should "run function several times if failed with wait" in {
    logErrorsFunctionMock expects "Tried 3 times, still not enough : failed"

    val result = Retry.withWait(3, 3000, logErrorsFunctionMock) {
      throw new Exception("failed")
    }

    result.failed. map { _ shouldBe a[Exception] }
  }

  "Retry" should "return result if no error with wait" in {
      val result = Retry.withWait(3, 3000, logErrorsFunctionMock) {
        1+1
      }

    result map { _ shouldBe 2 }
  }
}