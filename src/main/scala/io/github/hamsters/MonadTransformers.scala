package io.github.hamsters

import scala.concurrent.{ExecutionContext, Future}

case class FutureOption[+A](future: Future[Option[A]]) extends AnyVal {
  def flatMap[B](f: A => FutureOption[B])(implicit ec: ExecutionContext): FutureOption[B] = {
    val newFuture = future.flatMap{
      case Some(a) => f(a).future
      case None => Future.successful(None)
    }
    FutureOption(newFuture)
  }

  def map[B](f: A => B)(implicit ec: ExecutionContext): FutureOption[B] = {
    FutureOption(future.map(option => option map f))
  }
}

case class FutureEither[L, +R](future: Future[Either[L,R]]) extends AnyVal {
  def flatMap[R2](f: R => FutureEither[L,R2])(implicit ec: ExecutionContext): FutureEither[L,R2] = {
    val newFuture = future.flatMap{
      case Right(r) => f(r).future
      case Left(l) => Future.successful(Left(l))
    }
    FutureEither(newFuture)
  }

  def map[R2](f: R => R2)(implicit ec: ExecutionContext): FutureEither[L,R2] = {
    FutureEither(future.map(either => either.right map f))
  }
}