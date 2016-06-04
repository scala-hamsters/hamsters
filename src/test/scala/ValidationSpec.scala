import io.github.hamsters.Validation
import Validation._
import org.scalatest._

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give no failures" in {
    val e1 = OK(1)
    val e2 = OK(2)
    val e3 = OK(3)

    val validation = Validation(e1, e2, e3)
    val failures: List[String] = validation.failures
    validation.hasFailures should be(false)
    failures should be(Nil)
  }

  "Validation" should "give failures" in {
    val e1 = OK(1)
    val e2 = KO("nan")
    val e3 = KO("nan2")

    val validation = Validation(e1, e2, e3)
    val failures: List[String] = validation.failures
    validation.hasFailures should be(true)
    failures should be(List("nan", "nan2"))
  }

  "Validation" should "give failures with mixed types" in {
    val e1 = OK(1)
    val e2 = OK("2")
    val e3 = KO("nan")

    val validation = Validation(e1, e2, e3)
    validation.hasFailures should be(true)
    validation.failures should be(List("nan"))
  }

  "OK" should "give a value using get and getOrElse" in {
    val e = OK(1)
    e.get should be(1)
    e.getOrElse(2) should be(1)
  }

  "KO" should "give a value using getOrElse" in {
    val e = KO("d'oh!")
    e.getOrElse(2) should be(2)
  }

  "Either" should "compose using flatMap and map" in {
    val e1 = OK(1)
    val e2 = OK(2)
    val e3 = OK(3)

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield s"$v1-$v2-$v3"

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
    } yield s"$v1-$v2-$v3"

    combine should be(KO("nan"))

  }

}