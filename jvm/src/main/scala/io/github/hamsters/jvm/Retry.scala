package io.github.hamsters.jvm

import scala.concurrent.{ExecutionContext, Future, blocking}

object Retry {

  /**
    * Retry a function several times
    *
    * @param maxTries           number of retries
    * @param waitInMilliSeconds number of milliseconds to wait before retrying
    * @param errorFn            the function to run if it fails, to handle the error message
    * @param fn                 the function to run
    * @tparam T
    * @return Future result
    */
  def withWait[T](maxTries: Int, waitInMilliSeconds: Int, errorFn: String => Unit = _ => Unit)(fn: => T)
                 (implicit executionContext: ExecutionContext): Future[T] = {
    def retry(remainingTries: Int, waitInMilliSeconds: Int, errorFn: String => Unit)(fn: => T): Future[T] = {
      Future {
        if (remainingTries < maxTries) blocking(Thread.sleep(waitInMilliSeconds))
        fn
      }.recoverWith {
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
