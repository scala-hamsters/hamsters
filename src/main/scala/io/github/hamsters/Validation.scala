package io.github.hamsters

import scala.util.{Left, Right}

class Validation(eithers: List[Either[_,_]]) {
  def failures = eithers.collect { case l: Left[_, _] => l.left.get }

  def successes = eithers.collect { case r: Right[_, _] => r.right.get }
}

object Validation {

  val OK = Right
  val KO = Left

  def apply[L, R](eithers: Either[L, R]*) = new Validation(eithers.toList)

  implicit class OKBiasedEither[L, R](e: Either[L, R]) {
    def map[R2](f: R => R2) = e.right.map(f)

    def flatMap[R2](f: R => Either[L, R2]) = e.right.flatMap(f)

    def filter(p: (R) => Boolean) = filterWith(p)

    def filterWith(p: (R) => Boolean) = e.right.filter(p)
  }
}


