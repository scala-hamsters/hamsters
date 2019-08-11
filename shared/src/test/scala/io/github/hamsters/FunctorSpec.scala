package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}
import scala.language.higherKinds
import scala.reflect.ClassTag

class OptionFunctorSpec extends FunctorSpec(Functor.functorOption)
class ListFunctorSpec extends FunctorSpec(Functor.functorList)

abstract class FunctorSpec[Box[_]](val functor: Functor[Box])(implicit val arbitrary: Arbitrary[Box[Int]], tag: ClassTag[Box[_]]) extends Properties(s"Functor for $tag")  with TypeUtils {

  import functor._

  val mapF: Box[A] => Box[B] = map(_)(f)
  val mapG: Box[B] => Box[C] = map(_)(g)

  val mapH: Box[C] => Box[D] = map(_)(h)

  val mapFG: Box[A] => Box[C] = map(_)(fg)
  val mapGH: Box[B] => Box[D] = map(_)(gh)


  // map_id == id
  property("identity") = forAll { boxA: Box[Int] =>
    map(boxA)(identity) == boxA
  }

  property("composition") = forAll { boxA: Box[Int] =>
    mapFG(boxA) == mapG(mapF(boxA))
  }

  property("associativity") = forAll { boxA: Box[Int] =>
    (mapF andThen mapGH)(boxA) == (mapFG andThen mapH)(boxA)
  }

}
