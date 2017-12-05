package io.github.hamsters

import java.util.Date
import org.scalatest.{FlatSpec, Matchers}

class ShowSpec extends FlatSpec with Matchers {

  @ShowMacro
  case class Name(firstName: String, lastName: String)

  @ShowMacro
  case class Person(name : Name, age: Int)

  @ShowMacro
  case class AdditionalShowInstances(
                                      c: Char = '1',
                                      b: Byte = 1.toByte,
                                      boo: Boolean = true,
                                      l: Long = 1L,
                                      doub: Double = 1.0,
                                      fl: Float = 1f,
                                      date: Date = new Date(11111),
                                      short: Short = 1.toShort)

  "Show on simple object" should "show field names and values of object" in {
    val n = Name("john", "doe")
    Show.show(n) should be("Name(firstName=john,lastName=doe)")
  }

  "Show on deeper object" should "show field names and values of object" in {
    val p = Person(Name("john", "doe"), 35)
    Show.show(p) should be("Person(name=Name(firstName=john,lastName=doe),age=35)")
  }

  "Show on additional types" should "show field names and values of object" in {
    """
      |Show.show(AdditionalShowInstances(
      |    '1',
      |      1.toByte,
      |      true,
      |      1L,
      |      1.0,
      |      1f,
      |      new Date(11111),
      |      1.toShort))
    """.stripMargin should compile
  }

  case object Foo
  "Show on case object" should "show simple class name without $" in {
    Show.show(Foo.getClass) should be("Foo")
  }

  "Show on case class" should "show simple class name without $" in {
    Show.show(Name("hamster", "hidden").getClass) should be("Name")
  }

  "ShowableSyntax" should "add show method on supported types" in {
    import ShowableSyntax.ShowableOps
    Name("Hamster", "Hiden").getClass.show should be("Name")
  }

}
