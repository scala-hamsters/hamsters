import org.scalatest.{Matchers, FlatSpec}
import io.github.hamsters.Implicits
import io.github.hamsters.Validation._

class ImplicitsSpec extends FlatSpec with Matchers{

  "Either" should "compose using flatMap and map" in {

    import Implicits._

    val e1 = OK(1)
    val e2 = OK(2)
    val e3 = OK(3)

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield(s"$v1-$v2-$v3")

    combine should be(OK("1-2-3"))

  }

  "Either" should "stop at first error" in {

    import Implicits._

    val e1: Either[String, Int] = OK(1)
    val e2: Either[String, Int] = KO("nan")
    val e3: Either[String, Int] = KO("nan2")

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield(s"$v1-$v2-$v3")

    combine should be(KO("nan"))

  }

}
