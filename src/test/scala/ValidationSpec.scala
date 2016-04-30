import io.github.hamsters.Validation
import Validation._
import org.scalatest._

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give failures and successes" in {
    val e1 = OK(1)
    val e2 = KO("nan")
    val e3 = KO("nan2")

    val validation = Validation(e1, e2, e3)
    validation.failures should be(List("nan", "nan2"))
    validation.successes should be(List(1))
  }

  "Either" should "compose using flatMap and map" in {

    val e1 = OK(1)
    val e2 = OK(2)
    val e3 = OK(3)

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield (s"$v1-$v2-$v3")

    combine should be(OK("1-2-3"))

  }

  "Either" should "stop at first error" in {

    val e1: Either[String, Int] = OK(1)
    val e2: Either[String, Int] = KO("nan")
    val e3: Either[String, Int] = KO("nan2")

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield (s"$v1-$v2-$v3")

    combine should be(KO("nan"))

  }

}