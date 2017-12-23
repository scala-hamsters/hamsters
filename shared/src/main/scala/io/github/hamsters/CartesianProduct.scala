package io.github.hamsters

import scala.language.higherKinds

trait CartesianProduct[Box[_]] {
  def product[A, B](a: Box[A], b: Box[B]): Box[(A, B)]
}

object CartesianProduct {

  implicit def semigroupalOption = new CartesianProduct[Option] {
    override def product[A, B](maybeA: Option[A], maybeB: Option[B]): Option[(A, B)] = (maybeA, maybeB) match {
      case (Some(a), Some(b)) => Some(a -> b)
      case _ => None
    }
  }

  implicit def semigroupalList = new CartesianProduct[List] {
    override def product[A, B](listA: List[A], listB: List[B]): List[(A, B)] = for {
      a <- listA
      b <- listB
    } yield a -> b
  }
}