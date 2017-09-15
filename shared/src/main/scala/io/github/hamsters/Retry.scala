package io.github.hamsters

import scala.util.Try

object Retry {
  // Returning T, throwing the exception on failure
  final def retry[T](maxRetries: Int, errorFn: (String) => Unit = _=> Unit)(fn: => T): T = {
    @annotation.tailrec
    def retry(maxRetries: Int,nbTries: Int, errorFn: (String) => Unit)(fn: => T): T = {
      util.Try {fn} match {
        case util.Success(x) => x
        case _ if maxRetries > 1 => retry(maxRetries, maxRetries - 1,errorFn)(fn)
        case util.Failure(e) =>
          errorFn(s"Tried $maxRetries times, still not enough : ${e.getMessage}")
          throw e
      }
    }
    retry(maxRetries,maxRetries,errorFn)(fn)
  }
}
