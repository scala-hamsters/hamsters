import org.scalatest.{Matchers, FlatSpec}
import io.github.hamsters.Implicits

class ImplicitsSpec extends FlatSpec with Matchers{

  "Validation" should "give failures and successes" in {

    import Implicits._

    val e1: Either[String, Int] = Right(1)
    val e2: Either[String, Int] = Left("nan")
    val e3: Either[String, Int] = Left("nan2")

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield(s"$v1-$v2-$v3")

    combine should be(Left("nan"))

  }

}
