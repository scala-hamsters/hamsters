package io.github.hamsters

import scala.util.{Left, Right}

class Validation[L](eithers: List[Either[L,_]]) {
  val failures: List[L] = eithers.collect { case l: Left[L, _] => l.left.get }
  val hasFailures: Boolean = failures.nonEmpty
}

object Validation {

  val OK = Right
  val KO = Left

  def apply[L, R](eithers: Either[L, _]*) = new Validation(eithers.toList)

  def fromCatchable[R](body: => R): Either[String,R] = {
     val throwableToErrorMessage = (t: Throwable) => Option(t.getMessage).getOrElse("Error: an exception has been thrown")
     fromCatchable(body, throwableToErrorMessage)
  }

  def fromCatchable[L, R](body: => R, errorMgt: (Throwable) => L): Either[L,R] = {
    try {
      Right(body)
    }
    catch {
      case t: Throwable => Left(errorMgt(t))
    }
  }

  implicit class OKBiasedEither[L, R](e: Either[L, R]) {
    def map[R2](f: R => R2) = e.right.map(f)

    def flatMap[R2](f: R => Either[L, R2]) = e.right.flatMap(f)

    def filter(p: (R) => Boolean) = filterWith(p)

    def filterWith(p: (R) => Boolean) = e.right.filter(p)

    def get = e.right.get

    def getOrElse[R2 >: R](or: => R2) = e.right.getOrElse(or)
  }
}
