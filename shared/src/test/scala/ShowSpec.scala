import io.github.hamsters.{Show, ShowableSyntax, Person, Name}
import org.scalatest.{FlatSpec, Matchers}

class ShowSpec extends FlatSpec with Matchers {

  "Show" should "show field names and values of object" in {
    val p = Person(Name("john", "doe"), 35)
    implicit val personShowable = Show.format[Person]
    Show.print(p) // see console output
    Show.show(p) should be("Person(name = Name(firstName=john, lastName=doe), age = 35)")
  }

  "Show" should "show field names and values of object with syntactic sugar" in {
    val p = Person(Name("john", "doe"), 35)
    implicit val personShowable = Show.format[Person]
    import ShowableSyntax._
    p.print // see console output
    p.show should be("Person(name = Name(firstName=john, lastName=doe), age = 35)")
  }

}