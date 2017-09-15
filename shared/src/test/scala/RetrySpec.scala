import io.github.hamsters.Retry
import org.scalatest.{FlatSpec, Matchers}
import scala.util.Success
import org.scalamock.scalatest.MockFactory

class RetrySpec extends FlatSpec with Matchers with MockFactory {

  val logErrorsFunctionMock = mockFunction[String, Unit]

  "Retry" should "run function several times if failed" in {
    logErrorsFunctionMock expects ("Tried 3 times, still not enough : failed")
    
    Retry.retry(3, logErrorsFunctionMock) {
      throw new Exception("failed")
    }
    
  }

  "Retry" should "return result if no error" in {
    val result = Retry.retry(3, logErrorsFunctionMock) {
      1+1
    }

    result should be(Success(2))
  }

}