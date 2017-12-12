package io.github.hamsters

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}

import scala.reflect.ClassTag

class OptionMonadSpec extends MonadSpec[String, String, String, Option](Monad.optionMonad)

//exceptions break the laws
//class FutureMonadSpec extends MonadSpec[String, String, String, Future](Monad.futureMonad)

abstract class MonadSpec[A: Arbitrary, B: Arbitrary, C: Arbitrary, Box[_]](monad: Monad[Box])
(implicit boxArb: Arbitrary[Box[A]], aToBoxBArb: Arbitrary[A => Box[B]], bToBoxCArb: Arbitrary[B => Box[C]], tag: ClassTag[Box[_]])

extends Properties(s"Monad for $tag") {

  property("left identity") = forAll { (f: (A => Box[B]), a: A) =>
    val boxA: Box[A] = monad.pure(a)
    monad.flatMap(boxA)(f) == f(a)
  }

  property("right identity") = forAll { (a: A, f: (A => Box[B])) =>
    monad.flatMap(monad.pure(f(a)))(monad.pure) == monad.pure(f(a))
  }

  property("associativity") = forAll { (f: (A => Box[B]), g: (B => Box[C]), box: Box[A]) =>
    val boxB: Box[B] = monad.flatMap(box)(f)
    monad.flatMap(boxB)(g) == monad.flatMap(box) { a =>
      val boxB: Box[B] = f(a)
      monad.flatMap(boxB)(g)
    }
  }

}