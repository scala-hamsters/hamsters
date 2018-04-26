import io.github.hamsters.GenLens

@GenLens
case class Street(number: Int, name: String)
@GenLens
case class Address(street: Street)
@GenLens
case class Person(firstName: String, lastName: String, address: Address)

val instance = Person("John", "Doe", Address(Street(12, "Rue de Picpus")))







instance.copy(address = instance.address.copy(street = instance.address.street.copy(number = 42)))

import Street._
import Address._
import Person._

(_address composeLens  _street composeLens _number).set(instance)(42)
