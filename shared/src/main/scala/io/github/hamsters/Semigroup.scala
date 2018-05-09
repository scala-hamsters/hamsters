package io.github.hamsters

import scala.language.implicitConversions

trait Semigroup[T] {
  def combine(a: T, b: T): T
}

object Semigroup {

  def apply[T](implicit m : Semigroup[T]) = m

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

  implicit def semigroupalMap[K,V]: Semigroup[Map[K,V]] = new Semigroup[Map[K,V]] {
    override def combine(a: Map[K,V], b: Map[K,V]) = a ++ b
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





