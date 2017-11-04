package io.github.hamsters

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Retry {
 
  /**
   * Retry a function several times
   * @param maxTries number of retries
   * @param errorFn the function to run if it fails, to handle the error message
   * @param fn the function to run
   * @tparam T
   * @return Try of result
   */
  def apply[T](maxTries: Int, errorFn: (String) => Unit = _=> Unit)(fn: => T): Try[T] = {
    fromTry(maxTries, errorFn)(Try(fn))
  }

  /**
    * Retry a function several times
    * @param maxTries number of retries
    * @param errorFn the function to run if it fails, to handle the error message
    * @param fn the function to run, returning a Try
    * @tparam T
    * @return Try of result
    */
  def fromTry[T](maxTries: Int, errorFn: (String) => Unit = _=> Unit)(fn: => Try[T]): Try[T] = {
    @annotation.tailrec
    def retry(remainingTries: Int, errorFn: (String) => Unit)(fn: => Try[T]): Try[T] = {
      fn match {
        case Success(x) => Success(x)
        case _ if remainingTries > 1 => retry(remainingTries - 1, errorFn)(fn)
        case Failure(failure) =>
          errorFn(s"Tried $maxTries times, still not enough : ${failure.getMessage}")
          Failure(failure)
      }
    }
    retry(maxTries, errorFn)(fn)
  }

  /**
    * Retry a function several times
    * @param maxTries number of retries
    * @param waitInMilliSeconds number of milliseconds to wait before retrying
    * @param errorFn the function to run if it fails, to handle the error message
    * @param fn the function to run
    * @tparam T
    * @return Future result
    */
  def withWait[T](maxTries: Int, waitInMilliSeconds: Int, errorFn: (String) => Unit = _=> Unit)(fn: => T): Future[T] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    def retry(remainingTries: Int, waitInMilliSeconds: Int, errorFn: (String) => Unit)(fn: => T): Future[T] = {
      Future{
        if(remainingTries < maxTries) Thread.sleep(waitInMilliSeconds)
        fn
      }.recoverWith{
        case _ if remainingTries > 1 =>
          retry(remainingTries - 1, waitInMilliSeconds, errorFn)(fn)
        case failure =>
          errorFn(s"Tried $maxTries times, still not enough : ${failure.getMessage}")
          Future.failed(failure)
      }
    }
    retry(maxTries, waitInMilliSeconds, errorFn)(fn)
  }

}
