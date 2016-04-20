package io.github.hamsters

import scala.concurrent.{ExecutionContext, Future}

case class FutureOption[+A](future: Future[Option[A]]) extends AnyVal {
  def flatMap[B](f: A => FutureOption[B])(implicit ec: ExecutionContext): FutureOption[B] = {
    val newFuture = future.flatMap {
      case Some(a) => f(a).future
      case None => Future.successful(None)
    }
    FutureOption(newFuture)
  }

  def map[B](f: A => B)(implicit ec: ExecutionContext): FutureOption[B] = {
    FutureOption(future.map(option => option map f))
  }

  def filter(p: (A) ⇒ Boolean)(implicit ec: ExecutionContext): FutureOption[A] = filterWith(p)

  def filterWith(p: (A) ⇒ Boolean)(implicit ec: ExecutionContext): FutureOption[A] = {
    FutureOption(future.map {
      case Some(a) => if (p(a)) Some(a) else None
      case _ => None
    })
  }
}

case class FutureEither[L, +R](future: Future[Either[L, R]]) extends AnyVal {
  def flatMap[R2](f: R => FutureEither[L, R2])(implicit ec: ExecutionContext): FutureEither[L, R2] = {
    val newFuture = future.flatMap {
      case Right(r) => f(r).future
      case Left(l) => Future.successful(Left(l))
    }
    FutureEither(newFuture)
  }

  def map[R2](f: R => R2)(implicit ec: ExecutionContext): FutureEither[L, R2] = {
    FutureEither(future.map(either => either.right map f))
  }

  def filter(p: (R) ⇒ Boolean)(implicit ec: ExecutionContext): FutureEither[String, R] = filterWith(p)

  def filterWith(p: (R) ⇒ Boolean)(implicit ec: ExecutionContext): FutureEither[String, R] = {
    FutureEither(future.map {
      case Right(r) => if (p(r)) Right(r) else Left("No value matching predicate")
      case _ => Left("No value matching predicate")
    })
  }

  def filterWithDefault(p: (R) ⇒ Boolean, default: L)(implicit ec: ExecutionContext): FutureEither[L, R] = {
    FutureEither(future.map {
      case Right(r) => if (p(r)) Right(r) else Left(default)
      case _ => Left(default)
    })
  }

}