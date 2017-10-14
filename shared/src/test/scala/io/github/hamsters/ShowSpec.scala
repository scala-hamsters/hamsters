package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}
class ShowSpec extends FlatSpec with Matchers {

  "Show on simple object" should "show field names and values of object" in {
    val n = Name("john", "doe")
    Show.show(n) should be("Name(firstName=john,lastName=doe)")
  }

  "Show on deeper object" should "show field names and values of object" in {
    val p = Person(Name("john", "doe"), 35)
    Show.show(p) should be("Person(name=Name(firstName=john,lastName=doe),age=35)")
  }

}