package io.github.hamsters

import scala.concurrent.Future
import scala.util.{Try, Success, Failure}

case class FutureOption[A](override val wrapped: Future[Option[A]]) extends OptionT[A, Future](wrapped)

case class FutureTry[A](override val wrapped: Future[Try[A]]) extends TryT[A, Future](wrapped)

case class FutureEither[L, R](override val wrapped: Future[Either[L, R]]) extends EitherT[L, R, Future](wrapped)

class OptionT[A, Box[_]](val wrapped: Box[Option[A]]) {

  /**
    * Returns the result of applying f to this OptionT if this OptionT is not empty
    *
    * @param f
    * @tparam B
    * @return a new OptionT
    */
  def flatMap[B](f: A => OptionT[B, Box])(implicit evidence: Monad[Box]): OptionT[B, Box] = {
    val newBox = evidence.flatMap(wrapped) {
      case Some(a) => f(a).wrapped
      case None => evidence.pure(None: Option[B])
    }
    new OptionT(newBox)
  }

  /**
    * Returns the result of applying f to this OptionT if this OptionT is not empty
    *
    * @param f
    * @tparam B
    * @return a new OptionT
    */
  def map[B](f: A => B)(implicit evidence: Monad[Box]): OptionT[B, Box] = {
    new OptionT(evidence.map(wrapped)((option: Option[A]) => option.map(f)))
  }

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new OptionT
    */
  def filter(p: A ⇒ Boolean)(implicit evidence: Monad[Box]): OptionT[A, Box] = withFilter(p)

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new OptionT
    */
  def withFilter(p: A ⇒ Boolean)(implicit evidence: Monad[Box]): OptionT[A, Box] = {
    new OptionT(evidence.map(wrapped)(_.filter(p)))
  }
}

class TryT[A, Box[_]](val wrapped: Box[Try[A]]) {

  /**
    * Returns the result of applying f to this TryT if this TryT is not a Failure
    *
    * @param f
    * @tparam B
    * @return a new TryT
    */
  def flatMap[B](f: A => TryT[B, Box])(implicit evidence: Monad[Box]): TryT[B, Box] = {
    val newBox = evidence.flatMap(wrapped) {
      case Success(a) => f(a).wrapped
      case Failure(th) => evidence.pure(Failure(th): Try[B])
    }
    new TryT(newBox)
  }

  /**
    * Returns the result of applying f to this TryT if this TryT is not a Failure
    *
    * @param f
    * @tparam B
    * @return a new TryT
    */
  def map[B](f: A => B)(implicit evidence: Monad[Box]): TryT[B, Box] = {
    new TryT(evidence.map(wrapped)((tr: Try[A]) => tr.map(f)))
  }

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new TryT
    */
  def filter(p: A ⇒ Boolean)(implicit evidence: Monad[Box]): TryT[A, Box] = withFilter(p)

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new TryT
    */
  def withFilter(p: A ⇒ Boolean)(implicit evidence: Monad[Box]): TryT[A, Box] = {
    new TryT(evidence.map(wrapped)(_.filter(p)))
  }
}

class EitherT[L, R, Box[_]](val wrapped: Box[Either[L, R]]) {

  /**
    * Returns the result of applying f to this EitherT if this EitherT is not empty
    *
    * @param f
    * @tparam R2
    * @return a new EitherT
    */
  def flatMap[R2](f: R => EitherT[L, R2, Box])(implicit evidence: Monad[Box]): EitherT[L, R2, Box] = {
    val newBox: Box[Either[L, R2]] = evidence.flatMap(wrapped) {
      case Right(r) => f(r).wrapped
      case Left(l) => evidence.pure(Left(l))
    }
    new EitherT(newBox)
  }

  /**
    * Returns the result of applying f to this EitherT if this EitherT is not empty
    *
    * @param f
    * @tparam R2
    * @return a EitherT
    */
  def map[R2](f: R => R2)(implicit evidence: Monad[Box]): EitherT[L, R2, Box] = {
    val newBox: Box[Either[L, R2]] = evidence.map(wrapped)((either: Either[L, R]) => either.right.map(f))
    new EitherT(newBox)
  }

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new EitherT
    */
  def filter(p: R ⇒ Boolean)(implicit evidence: Monad[Box]): EitherT[String, R, Box] = withFilter(p)

  /**
    * Keep only values which satisfy a predicate
    *
    * @param p
    * @return a new EitherT
    */
  def withFilter(p: R ⇒ Boolean)(implicit evidence: Monad[Box]): EitherT[String, R, Box] = {
   val newBox: Box[Either[String, R]] = evidence.map(wrapped) {
      case Right(r) => if (p(r)) Right(r) else Left("No value matching predicate")
      case _ => Left("No value matching predicate")
    }
    new EitherT(newBox)
  }

  /**
    * Keep only values which satisfy a predicate, with a default value
    *
    * @param p
    * @param default
    * @return a new EitherT
    */
  def filterWithDefault(p: R ⇒ Boolean, default: L)(implicit evidence: Monad[Box]): EitherT[L, R, Box] = {
    val newBox: Box[Either[L, R]] =evidence.map(wrapped) {
      case Right(r) => if (p(r)) Right(r) else Left(default)
      case _ => Left(default)
    }
    new EitherT(newBox)
  }
}

object MonadTransformers {

  implicit def optionTToT[A, Box[_]](optionT: OptionT[A, Box]): Box[Option[A]] = optionT.wrapped

  implicit def tryTToT[A, Box[_]](tryT: TryT[A, Box]): Box[Try[A]] = tryT.wrapped

  implicit def eitherTToT[L, R, Box[_]](eitherT: EitherT[L, R, Box]): Box[Either[L, R]] = eitherT.wrapped

  implicit def futureOptionToFuture[A](fo: FutureOption[A]): Future[Option[A]] = fo.wrapped

  implicit def futureTryToFuture[A](fo: FutureTry[A]): Future[Try[A]] = fo.wrapped

  implicit def futureEitherToFuture[L, R](fe: FutureEither[L, R]): Future[Either[L, R]] = fe.wrapped
}
