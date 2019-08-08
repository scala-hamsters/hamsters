package io.github.hamsters

import scala.collection.mutable

object Memo {

  /**
    * Memoize results for idempotent functions
    * For a thread safe implementation see Memo.threadSafeMemoize
    * @param f
    * @tparam A input type
    * @tparam B output type
    * @return calcluated, or memoized result if it has already been calculated
    */
  def memoize[A, B](f: A => B): A => B = new mutable.HashMap[A, B]() {
    override def apply(key: A): B = getOrElseUpdate(key, f(key))
  }

  /**
    * Memoize results for idempotent functions with a thread safe structure
    * @param f
    * @tparam A input type
    * @tparam B output type
    * @return calcluated, or memoized result if it has already been calculated
    */
  def threadSafeMemoize[A, B](f: A => B): A => B = new mutable.HashMap[A, B]() { self =>
    override def apply(key: A): B = self.synchronized(getOrElseUpdate(key, f(key)))
  }

}
