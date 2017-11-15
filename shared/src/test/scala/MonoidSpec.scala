import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

import scala.reflect._
import _root_.io.github.hamsters.Monoid


class BooleanMonoidSpec extends MonoidSpec[Boolean](Monoid.booleaMonoids)
class IntMonoidSpec extends MonoidSpec[Int](Monoid.intMonoid)
class BigDecimalMonoidSpec extends MonoidSpec[BigDecimal](Monoid.bigDecimalMonoid)
//float and double monoid break the laws : https://github.com/scalaz/scalaz/issues/334
//class FloatMonoidSpec extends MonoidSpec[Float](Monoid.floatMonoid)
//class DoubleMonoidSpec extends MonoidSpec[Double](Monoid.doubleMonoid)
class StringMonoidSpec extends MonoidSpec[String](Monoid.stringMonoid)
class ListeMonoidSpec extends MonoidSpec[List[String]](Monoid.listMonoid)
class SeqMonoidSpec extends MonoidSpec[Seq[String]](Monoid.seqMonoid)
class OptionMonoidSpec extends MonoidSpec[Option[List[Int]]](Monoid.optionMonoid)


abstract class MonoidSpec[T  : ClassTag](monoid: Monoid[T])(implicit arbitrary: Arbitrary[T]) extends Properties(s"Monoid for ${classTag[T]}") {

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
