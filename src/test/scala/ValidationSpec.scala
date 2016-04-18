import io.github.hamsters.{Validation}
import org.scalatest._

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give failures and successes" in {
    val e1: Either[String, Int] = Right(1)
    val e2: Either[String, Int] = Left("nan")
    val e3: Either[String, Int] = Left("nan2")

    val validation = Validation(e1,e2, e3)
    validation.failures should be(List("nan", "nan2"))
    validation.successes should be(List(1))
  }

}