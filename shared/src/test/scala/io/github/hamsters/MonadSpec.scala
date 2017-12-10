package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect._

/* FIXME : diverging implicit expansion for type org.scalacheck.Arbitrary[(A, Box[B])]
class OptionMonadSpec extends MonadSpec[String, String, String, Option](Monad.optionMonad)

class FutureMonadSpec extends MonadSpec[String, String, String, Future](Monad.futureMonad)

abstract class MonadSpec[A, B, C, Box[_] : ClassTag](monad: Monad[Box])(implicit boxArb: Arbitrary[Box[A]], aArb: Arbitrary[A], bArb: Arbitrary[B], cArb: Arbitrary[C])
  extends Properties(s"Monad for ${classTag[Box[_]]}") {

  property("left identity") = forAll { (f: (A => Box[B]), a: A) =>
    val boxA: Box[A] = monad.pure(a)
    monad.flatMap(boxA)(f) == f(a)
  }

  property("right identity") = forAll { box: Box[A] =>
    monad.flatMap(box)(monad.pure) == monad
  }

  property("associativity") = forAll { (f: (A => Box[B]), g: (B => Box[C]), box: Box[A]) =>
    val boxB: Box[B] = monad.flatMap(box)(f)
    monad.flatMap(boxB)(g) == monad.flatMap(box) { a =>
      val boxB: Box[B] = f(a)
      monad.flatMap(boxB)(g)
    }
  }

}
*/