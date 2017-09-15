package io.github.hamsters

import scala.util.{Try, Success, Failure}

object Retry {
  // Returning T, throwing the exception on failure
  final def retry[T](maxRetries: Int, errorFn: (String) => Unit = _=> Unit)(fn: => T): Try[T] = {
    @annotation.tailrec
    def retry(nbTries: Int, errorFn: (String) => Unit)(fn: => T): Try[T] = {
      Try {fn} match {
        case Success(x) => Success(x)
        case _ if nbTries > 1 => retry(nbTries - 1, errorFn)(fn)
        case Failure(e) =>
          errorFn(s"Tried $maxRetries times, still not enough : ${e.getMessage}")
          Failure(e)
      }
    }
    retry(maxRetries,errorFn)(fn)
  }
}
