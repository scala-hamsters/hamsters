package io.github.hamsters

import scala.language.{higherKinds, implicitConversions}
import scala.concurrent.{ExecutionContext, Future}

trait Cartesian[Box[_]] {
  def product[A, B](a: Box[A], b: Box[B]): Box[(A, B)]
}

object Cartesian {

  implicit def cartesianOption = new Cartesian[Option] {
    override def product[A, B](maybeA: Option[A], maybeB: Option[B]): Option[(A, B)] = (maybeA, maybeB) match {
      case (Some(a), Some(b)) => Some(a -> b)
      case _ => None
    }
  }

  implicit def cartesianList = new Cartesian[List] {
    override def product[A, B](listA: List[A], listB: List[B]): List[(A, B)] = for {
      a <- listA
      b <- listB
    } yield a -> b
  }

  implicit def cartesianFuture(implicit ec: ExecutionContext) = new Cartesian[Future] {
    override def product[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] = for {
      a <- futureA
      b <- futureB
    } yield a -> b
  }

  case class Tuple2Box[Box[_], T](boxA: Box[T], boxB: Box[T]) {
    def mapN(f: (T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] = {
      functor.map(s.product(boxA, boxB)) { case (a, b) => f(a, b) }
    }
  }

  case class Tuple3Box[Box[_], T](boxA: Box[T], boxB: Box[T], boxC: Box[T]) {
    def mapN(f: (T, T, T) => T)(implicit functor: Functor[Box], cartesianProduct: Cartesian[Box]): Box[T] = {
      functor.map(cartesianProduct.product(cartesianProduct.product(boxA, boxB), boxC)) { case ((a, b), c) => f(a, b, c) }
    }
  }

  implicit def t2x[Box[_], T](t: (Box[T], Box[T])) = Tuple2Box(t._1, t._2)
  implicit def t3x[Box[_], T](t: (Box[T], Box[T], Box[T])) = Tuple3Box(t._1, t._2, t._3)
}

