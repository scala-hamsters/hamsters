
# Lenses


A Lens helps to deal with a problem of updating complex immutable nested objects.


```scala
case class Street(number: Int, name: String)
case class Address(street: Street)
case class Person(firstName: String, lastName: String, address: Address)
```

With copy, we will have to write : 

```scala
instance.copy(address = instance.address.copy(street = instance.address.street.copy(number = 42)))
res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))
```

With Lens, we define a `Lens` for each field we want to focus on (typically in the
case class companion object) :


```scala
case class Street(number: Int, name: String)
object Street {
  val _number = new Lens[Street, Int] {
    override def get: Street => Int = _.number
    override def set: Street => Int => Street = s => v => s.copy(number = v)
  }
}

case class Address(street: Street)
object Address {
  val _street = new Lens[Address, Street] {
    override def get: Address => Street = _.street
    override def set: Address => Street => Address = s => v => s.copy(street = v)
  }
}

case class Person(firstName: String, lastName: String, address: Address)
object Person {
  val _address = new Lens[Person, Address] {
    override def get: Person => Address = _.address
    override def set: Person => Address => Person = s => v => s.copy(address = v)
  }
}
```

And to use lenses  : 

```scala
import Street._
import Address._
import Person._

(_address composeLens  _street composeLens _number).set(instance)(42) 
//res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))

```

You can also use a more consise version


```scala
import Street._
import Address._
import Person._

(_address >>>  _street >>> _number).set(instance)(42) 
//res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))

```
