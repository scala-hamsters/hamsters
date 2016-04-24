package io.github.hamsters

import scala.util.{Left, Right}

class Validation[L, R](eithers: List[Either[L, R]]) {
  def failures: List[L] = eithers.collect { case l: Left[L, _] => l.left.get }

  def successes: List[R] = eithers.collect { case r: Right[_, R] => r.right.get }
}

object Validation {
  val OK = Right
  val KO = Left
  def apply[L, R](eithers: Either[L, R]*) = new Validation(eithers.toList)
}


