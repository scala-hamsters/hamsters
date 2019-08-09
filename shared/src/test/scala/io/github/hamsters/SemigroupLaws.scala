package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

import scala.language.higherKinds
import scala.reflect.ClassTag

class SemigroupStringLaws extends SemigroupLaws(Semigroup.semigroupalString)
class SemigroupIntLaws extends SemigroupLaws(Semigroup.semigroupalInt)
class SemigroupListStringLaws extends SemigroupLaws(Semigroup.semigroupalSeq[String])
class SemigroupMapStringLaws extends SemigroupLaws(Semigroup.semigroupalMap[String,Int])
class SemigroupOptionStringLaws extends SemigroupLaws(Semigroup.semigroupalOption[String])

abstract class SemigroupLaws[T](semigroup: Semigroup[T])(implicit a1: Arbitrary[T], tag: ClassTag[T]) extends Properties(s"Semigroup for $tag}") {

  // (x |+| y) |+| z = x |+| (y |+| z)
  property("associativity") = forAll { (x : T , y : T, z : T) =>
    import semigroup._
    combine(x,combine(y,z)) == combine(combine(x,y), z)
  }
}
