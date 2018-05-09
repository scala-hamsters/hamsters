package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

import scala.reflect._

class BooleanMonoidLaws extends MonoidLaws[Boolean](Monoid.booleanMonoid)
class IntMonoidLaws extends MonoidLaws[Int](Monoid.intMonoid)
class BigDecimalMonoidLaws extends MonoidLaws[BigDecimal](Monoid.bigDecimalMonoid)
//float and double monoid break the laws : https://github.com/scalaz/scalaz/issues/334
//class FloatMonoidLaws extends io.github.hamsters.MonoidLaws[Float](Monoid.floatMonoid)
//class DoubleMonoidLaws extends io.github.hamsters.MonoidLaws[Double](Monoid.doubleMonoid)
class StringMonoidLaws extends MonoidLaws[String](Monoid.stringMonoid)
class ListeMonoidLaws extends MonoidLaws[List[String]](Monoid.listMonoid)
class SeqMonoidLaws extends MonoidLaws[Seq[String]](Monoid.seqMonoid)
class OptionMonoidLaws extends MonoidLaws[Option[List[Int]]](Monoid.optionMonoid)

abstract class MonoidLaws[T: ClassTag](monoid: Monoid[T])(implicit arbitrary: Arbitrary[T]) extends Properties(s"Monoid for ${classTag[T]}") {

  val id: T = monoid.empty

  // o mean compose
  // n o id == id o n == n
  property("identity") = forAll { n: T =>
    monoid.compose(n, id) == n && monoid.compose(id, n) == n
  }

  // forall l, r => l o r
  property("composition") = forAll { (l: T, r: T) =>
    monoid.compose(l, r).isInstanceOf[T]
  }

  // l o (m o r) == (l o m) o r
  property("associativity") = forAll { (l: T, m: T, r: T) =>
    val lm = monoid.compose(l,m)
    val mr = monoid.compose(m,r)
    monoid.compose(lm, r) == monoid.compose(l, mr)
  }

}
