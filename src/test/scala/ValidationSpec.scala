import io.github.hamsters.Validation
import io.github.hamsters.Validation._
import org.scalatest._

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give failures and successes" in {
    val e1 = OK(1)
    val e2 = KO("nan")
    val e3 = KO("nan2")

    val validation = Validation(e1,e2, e3)
    validation.failures should be(List("nan", "nan2"))
    validation.successes should be(List(1))
  }

}