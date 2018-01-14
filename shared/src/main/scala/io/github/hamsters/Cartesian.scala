package io.github.hamsters

import scala.language.{higherKinds, implicitConversions}
import scala.concurrent.{ExecutionContext, Future}

trait Cartesian[Box[_]] {
  def product[A, B](a: Box[A], b: Box[B]): Box[(A, B)]
}

@CartesianMacro
object Cartesian {

  implicit def cartesianOption : Cartesian[Option] = new Cartesian[Option] {
    override def product[A, B](maybeA: Option[A], maybeB: Option[B]): Option[(A, B)] = (maybeA, maybeB) match {
      case (Some(a), Some(b)) => Some(a -> b)
      case _ => None
    }
  }

  implicit def cartesianList:Cartesian[List] = new Cartesian[List] {
    override def product[A, B](listA: List[A], listB: List[B]): List[(A, B)] = for {
      a <- listA
      b <- listB
    } yield a -> b
  }

  implicit def cartesianFuture(implicit ec: ExecutionContext): Cartesian[Future] = new Cartesian[Future] {
    override def product[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] = for {
      a <- futureA
      b <- futureB
    } yield a -> b
  }





}

