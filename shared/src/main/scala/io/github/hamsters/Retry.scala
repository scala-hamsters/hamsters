package io.github.hamsters

import scala.util.Try

object Retry {


  
  /**
   * Retry a function n times
   * @param n number of retries
   * @return Try of result
   */
  @annotation.tailrec
  def retry[T](n: Int)(fn: => T): util.Try[T] = {
    // Borrowed from https://stackoverflow.com/questions/7930814/whats-the-scala-way-to-implement-a-retry-able-call-like-this-one
    util.Try { fn } match {
      case x: util.Success[T] => x
      case _ if n > 1 => retry(n - 1)(fn)
      case fn => fn
    }
  }

  

}