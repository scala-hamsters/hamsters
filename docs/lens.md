
# Lenses


A Lens helps to deal with a problem of updating complex immutable nested objects.


```
case class Street(number: Int, name: String)
case class Address(street: Street)
case class Person(firstName: String, lastName: String, address: Address)
```

With copy, we will have to write : 

```
instance.copy(address = instance.address.copy(street = instance.address.street.copy(number = 42)))
res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))
```

With Lens, we  have to annotate the case class


```
@GenLens
case class Street(number: Int, name: String)
@GenLens
case class Address(street: Street)
@GenLens
case class Person(firstName: String, lastName: String, address: Address)
```

And to use lenses  : 

```
import Street._
import Address._
import Person._

(_address composeLens  _street composeLens _number).set(instance)(42) 
//res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))

```

You can also use a more consise version


```
import Street._
import Address._
import Person._

(_address >>>  _street >>> _number).set(instance)(42) 
//res1: Person = Person(John,Doe,Address(Street(42,Rue de Picpus)))

```
