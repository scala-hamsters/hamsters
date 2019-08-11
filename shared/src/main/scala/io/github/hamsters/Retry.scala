package io.github.hamsters

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
  def apply[T](maxTries: Int, errorFn: String => Unit = _=> Unit)(fn: => T): Try[T] =
    fromTry(maxTries, errorFn)(Try(fn))

  /**
    * Retry a function several times
    * @param maxTries number of retries
    * @param errorFn the function to run if it fails, to handle the error message
    * @param fn the function to run, returning a Try
    * @tparam T
    * @return Try of result
    */
  def fromTry[T](maxTries: Int, errorFn: String => Unit = _=> Unit)(fn: => Try[T]): Try[T] = {
    @annotation.tailrec
    def retry(remainingTries: Int, errorFn: String => Unit)(fn: => Try[T]): Try[T] = {
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

}
