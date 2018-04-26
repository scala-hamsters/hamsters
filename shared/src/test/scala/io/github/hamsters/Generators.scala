package io.github.hamsters

import org.scalacheck.{Arbitrary, Gen}

object Generators {
  implicit val arbitraryStreet= Arbitrary[Street] {
    for {
      number <-  Gen.choose[Int](0, Int.MaxValue)
      street <- Gen.alphaStr
    } yield Street(number, street)
  }

  implicit val arbitraryUser = Arbitrary[User]{
    for {
      firstName <- Gen.alphaStr
      lastName <- Gen.alphaStr
      street <- arbitraryStreet.arbitrary
    } yield User(firstName, lastName, street)
  }

}
