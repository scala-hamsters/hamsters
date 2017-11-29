package io.github.hamsters

trait Functor[Box[_]] {

  def map[A, B](boxA: Box[A])(f: A => B): Box[B]

}