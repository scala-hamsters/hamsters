# Show

Show provides a textual representation. You provide a `Showable` instance for your case class
(typically as an implicit in its companion object).

```scala
case class Name(firstName: String, lastName: String)
object Name {
  implicit val showable: Showable[Name] = new Showable[Name] {
    override def format(a: Name): String =
      "Name(firstName=" + Show.show(a.firstName) + ",lastName=" + Show.show(a.lastName) + ")"
  }
}

val n = Name("john", "doe")
Show.show(n) // "Name(firstName=john,lastName=doe)
```


It also composes with nested case classes (each nested type needs its own `Showable` instance):

```scala
case class Person(name: Name, age: Int)
object Person {
  implicit val showable: Showable[Person] = new Showable[Person] {
    override def format(a: Person): String =
      "Person(name=" + Show.show(a.name) + ",age=" + Show.show(a.age) + ")"
  }
}

val p = Person(Name("john", "doe"), 35)
Show.show(p) // "Person(name=Name(firstName=john,lastName=doe),age=35)"

// ShowableSyntax add show method
import ShowableSyntax.ShowableOps
Person(Name("john", "doe"), 35).show // "Person(name=Name(firstName=john,lastName=doe),age=35)"
```


It also works on the runtime class of case classes and case objects:

```scala
Show.show(Name("hamster", "hiden").getClass) // "Name"

case object Foo
Show.show(Foo.getClass) // "Foo"
```
