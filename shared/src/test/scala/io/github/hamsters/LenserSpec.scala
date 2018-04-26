package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

@GenLens
case class Street(number: Int, name: String)
@GenLens
case class User(firstName: String, lastName: String, street: Street)

class LenserSpec extends Properties("Lens laws") {

  import Generators._
  import Street._

  property("if I get twice, I get the same answer") = forAll { (user: User) =>
    _number.get(user.street) == _number.get(user.street)
  }

  property("if I get, then set it back, nothing changes") = forAll { (user: User) =>
    _number.set(user.street)(_number.get(user.street)) == user.street
  }

  property("if I set, then get, I get what I set") = forAll { (user: User, streetNumber : Int) =>
    _number.get(_number.set(user.street)(streetNumber)) == streetNumber
  }

  property("if I set twice then get, I get the second thing I set") = forAll { (user: User, streetNumber1 : Int, streetNumber2 : Int) =>
    _number.get(_number.set(_number.set(user.street)(streetNumber1))(streetNumber2)) == streetNumber2
  }


}



