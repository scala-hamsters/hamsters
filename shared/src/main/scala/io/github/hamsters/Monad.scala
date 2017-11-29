package io.github.hamsters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Monad[Box[_]] extends Functor[Box] {

  def pure[A](a: A): Box[A]

  def flatMap[A, B](boxA: Box[A])(f: A => Box[B]): Box[B]

}

object Monad {

  implicit val optionMonad = new Monad[Option] {

    override def pure[A](x: A): Option[A] = Some(x)

    override def flatMap[A, B](boxA: Option[A])(f: A => Option[B]) = boxA.flatMap(f)

    override def map[A, B](boxA: Option[A])(f: A => B) = boxA.map(f)
  }

  implicit val futureMonad = new Monad[Future] {

    override def pure[A](x: A): Future[A] = Future.successful(x)

    override def flatMap[A, B](boxA: Future[A])(f: A => Future[B]) = {
      boxA.flatMap(f)
    }

    override def map[A, B](boxA: Future[A])(f: A => B) = {
      boxA.map(f)
    }
  }
}

