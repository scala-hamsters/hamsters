package io.github.hamsters

import scala.collection.mutable

object Memo {

  /**
    * Memoize function result for idempotent functions
    * @param f
    * @tparam A input type
    * @tparam B output type
    * @return calcluated, or memoized result if it has already been calculated
    */
  def memoize[A, B](f: A => B): A => B = new mutable.HashMap[A, B]() {
    override def apply(key: A) = getOrElseUpdate(key, f(key))
  }

  /**
    * Memoize function result for idempotent functions with a thread safe structure
    * @param f
    * @tparam A input type
    * @tparam B output type
    * @return calcluated, or memoized result if it has already been calculated
    */
  def threadSafeMemoize[A, B](f: A => B): A => B = new mutable.HashMap[A, B]() { self =>
    override def apply(key: A) = self.synchronized(getOrElseUpdate(key, f(key)))
  }

}
