package io.github.hamsters

import scala.concurrent.{ExecutionContext, Future}

case class FutureOption[A](override val wrapped: Future[Option[A]]) extends OptionT[A, Future](wrapped)

class OptionT[A, Box[_]](val wrapped: Box[Option[A]]) {

  /**
    * Returns the result of applying f to this FutureOption if this FutureOption is not empty
    * @param f
    * @param ec
    * @tparam B
    * @return a new FutureOption
    */
  def flatMap[B](f: A => OptionT[B, Box])(implicit evidence: Monad[Box]): OptionT[B, Box] = {
    val newBox = evidence.flatMap(wrapped) {
      case Some(a) => f(a).wrapped
      case None => evidence.pure(None: Option[B])
    }
    new OptionT(newBox)
  }

  /**
    * Returns the result of applying f to this FutureOption if this FutureOption is not empty
    * @param f
    * @param ec
    * @tparam B
    * @return a new FutureOption
    */
  def map[B](f: A => B)(implicit evidence: Monad[Box]): OptionT[B, Box] = {
    new OptionT(evidence.map(wrapped)((option: Option[A]) => option.map(f)))
  }

  /**
    * Keep only values which satisfy a predicate
    * @param p
    * @param ec
    * @return a new FutureOption
    */
  def filter(p: (A) ⇒ Boolean)(implicit evidence: Monad[Box]): OptionT[A, Box] = withFilter(p)

  /**
    * Keep only values which satisfy a predicate
    * @param p
    * @param ec
    * @return a new FutureOption
    */
  def withFilter(p: (A) ⇒ Boolean)(implicit evidence: Monad[Box]): OptionT[A, Box] = {
    new OptionT(evidence.map(wrapped)(_.filter(p)))
  }
}

case class FutureEither[L, +R](future: Future[Either[L, R]]) extends AnyVal {

  /**
    * Returns the result of applying f to this FutureEither if this FutureEither is not empty
    * @param f
    * @param ec
    * @tparam R2
    * @return a new FutureEither
    */
  def flatMap[R2](f: R => FutureEither[L, R2])(implicit ec: ExecutionContext): FutureEither[L, R2] = {
    val newFuture = future.flatMap {
      case Right(r) => f(r).future
      case Left(l) => Future.successful(Left(l))
    }
    FutureEither(newFuture)
  }

  /**
    * Returns the result of applying f to this FutureEither if this FutureEither is not empty
    * @param f
    * @param ec
    * @tparam R2
    * @return a new FutureEither
    */
  def map[R2](f: R => R2)(implicit ec: ExecutionContext): FutureEither[L, R2] = {
    FutureEither(future.map(either => either.right map f))
  }

  /**
    * Keep only values which satisfy a predicate
    * @param p
    * @param ec
    * @return a new FutureEither
    */
  def filter(p: (R) ⇒ Boolean)(implicit ec: ExecutionContext): FutureEither[String, R] = withFilter(p)

  /**
    * Keep only values which satisfy a predicate
    * @param p
    * @param ec
    * @return a new FutureEither
    */
  def withFilter(p: (R) ⇒ Boolean)(implicit ec: ExecutionContext): FutureEither[String, R] = {
    FutureEither(future.map {
      case Right(r) => if (p(r)) Right(r) else Left("No value matching predicate")
      case _ => Left("No value matching predicate")
    })
  }

  /**
    * Keep only values which satisfy a predicate, with a default value
    * @param p
    * @param default
    * @param ec
    * @return a new FutureEither
    */
  def filterWithDefault(p: (R) ⇒ Boolean, default: L)(implicit ec: ExecutionContext): FutureEither[L, R] = {
    FutureEither(future.map {
      case Right(r) => if (p(r)) Right(r) else Left(default)
      case _ => Left(default)
    })
  }

}

object MonadTransformers {

  implicit def optionToT[A, Box[_]](optionT : OptionT[A, Box]): Box[Option[A]] = optionT.wrapped
  implicit def futureOptionToFuture[A](fe : FutureOption[A]): Future[Option[A]] = fe.wrapped
  implicit def futureEitherToFuture[L,R](fe : FutureEither[L,R]): Future[Either[L,R]] = fe.future
}
