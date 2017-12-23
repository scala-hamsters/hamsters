package io.github.hamsters

import scala.language.{higherKinds, implicitConversions}

trait Semigroup[T] {
  def combine(a: T, b: T): T
}

object Semigroup {

  implicit val semigroupalInt: Semigroup[Int] = new Semigroup[Int] {
    override def combine(a: Int, b: Int) = a + b
  }

  implicit val semigroupalString: Semigroup[String] = new Semigroup[String] {
    override def combine(a: String, b: String) = s"$a$b"
  }

  implicit def semigroupalSeq[T]: Semigroup[Seq[T]] = new Semigroup[Seq[T]] {
    override def combine(a: Seq[T], b: Seq[T]) = a ++ b
  }

  implicit def semigroupalList[T]: Semigroup[List[T]] = new Semigroup[List[T]] {
    override def combine(a: List[T], b: List[T]) = a ++ b
  }

  implicit def semigroupalOption[T](implicit semigroup: Semigroup[T]): Semigroup[Option[T]] = new Semigroup[Option[T]] {
    override def combine(maybeA: Option[T], maybeB: Option[T]) = {
      (maybeA, maybeB) match {
        case (Some(a), Some(b)) => Some(semigroup.combine(a, b))
        case _ => None
      }
    }
  }

}





case class Tuple2Box[Box[_], T](boxA: Box[T], boxB: Box[T]) {
  def mapN(f: (T, T) => T)(implicit functor : Functor[Box], s : CartesianProduct[Box]): Box[T] = {
    functor.map(s.product(boxA,boxB)){case (a,b)=> f(a,b)}
  }
}

case class Tuple3Box[Box[_], T](boxA: Box[T], boxB: Box[T], boxC : Box[T]) {
  def mapN(f: (T, T, T) => T)(implicit functor : Functor[Box], s : CartesianProduct[Box]): Box[T] = {
    functor.map(s.product(s.product(boxA,boxB), boxC)){case ((a, b), c) => f(a,b,c)}
  }
}

object TupleBox {
  implicit def t2x[Box[_],T](t: (Box[T], Box[T])) = Tuple2Box(t._1, t._2)
  implicit def t3x[Box[_],T](t: (Box[T], Box[T], Box[T])) = Tuple3Box(t._1, t._2, t._3)
}