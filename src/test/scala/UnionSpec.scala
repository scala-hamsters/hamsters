import io.github.hamsters.{Union3Type, Union3}
import org.scalatest.{FlatSpec, Matchers}


class UnionSpec extends FlatSpec with Matchers{

  val jsonUnion = new Union3Type[String, Int, Double]
  import jsonUnion._

  "Union" should "work with different types" in {
    //json element can contain a String, a Int or a Double
    def jsonElement(x: Int): Union3[String, Int, Double] = {
      if(x >0) "0"
      else if (x % 2 == 0) 1
      else 2.0
    }

    jsonElement(1) match {
      case Union3(Some(v: String), None, None) =>
      case _ => fail("wrong union type")
    }

  }



}
