import io.github.hamsters.{Enumerable, Enumeration}
import org.scalatest.{FlatSpec, Matchers}

class EnumSpec extends FlatSpec with Matchers {

  "Enum" should "enumerate and parse an enumerable type" in {

    sealed trait Season
    case object Winter extends Season
    case object Spring extends Season
    case object Summer extends Season
    case object Fall extends Season

    implicit val seasonEnumerable = new Enumerable[Season] {
      override def list: List[Season] = List(Winter, Spring, Summer, Fall)
    }

    Enumeration.name(Winter) shouldBe "winter"
    Enumeration.parse("winter") shouldBe Some(Winter)

  }

  "Enum" should "enumerate and parse a custom enumerable type" in {

    sealed trait Season
    case object Winter extends Season
    case object Spring extends Season
    case object Summer extends Season
    case object Fall extends Season

    implicit val seasonEnumerable = new Enumerable[Season] {
      override def list = List(Winter, Spring, Summer, Fall)

      override def name(s: Season): String  = {
        s match {
          case Winter => "WINTER_SEASON"
          case other => super.name(other)
        }
      }
    }

    Enumeration.name(Winter) shouldBe "WINTER_SEASON"
    Enumeration.parse("WINTER_SEASON") shouldBe Some(Winter)

  }
}
