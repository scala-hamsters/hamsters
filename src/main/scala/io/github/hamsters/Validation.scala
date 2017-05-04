package io.github.hamsters

import scala.util.{Left, Right}

object Validation {

  val OK = Right
  val KO = Left

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


  def failures[L](eithers: Either[L, _]*): List[L] = eithers.toList.collect { case l: Left[L, _] => l.left.get }

  def hasFailures[L](eithers: Either[L, _]*): Boolean = failures(eithers: _*).nonEmpty

  implicit class OKBiasedEither[L, R](e: Either[L, R]) {
    def map[R2](f: R => R2) = e.right.map(f)

    def flatMap[R2](f: R => Either[L, R2]) = e.right.flatMap(f)

    def filter(p: (R) => Boolean) = filterWith(p)

    def filterWith(p: (R) => Boolean) = e.right.filter(p)

    def get = e.right.get

    def getOrElse[R2 >: R](or: => R2) = e.right.getOrElse(or)
  }

  def result[L,R1,R2](e1: Either[L,R1], e2: Either[L,R2]): Either[List[L], (R1,R2)] = {
    failures(e1, e2) match {
      case Nil => Right(e1.get, e2.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3]): Either[List[L], (R1,R2,R3)] = {
    failures(e1, e2, e3) match {
      case Nil => Right(e1.get, e2.get, e3.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3,R4](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3], e4: Either[L, R4]): Either[List[L], (R1,R2,R3,R4)] = {
    failures(e1, e2, e3, e4) match {
      case Nil => Right(e1.get, e2.get, e3.get, e4.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3,R4,R5](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3], e4: Either[L, R4], e5: Either[L, R5]): Either[List[L], (R1,R2,R3,R4,R5)] = {
    failures(e1, e2, e3, e4, e5) match {
      case Nil => Right(e1.get, e2.get, e3.get, e4.get, e5.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3,R4,R5,R6](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6]): Either[List[L], (R1,R2,R3,R4,R5,R6)] = {
    failures(e1, e2, e3, e4, e5, e6) match {
      case Nil => Right(e1.get, e2.get, e3.get, e4.get, e5.get, e6.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3,R4,R5,R6,R7](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7]): Either[List[L], (R1,R2,R3,R4,R5,R6,R7)] = {
    failures(e1, e2, e3, e4, e5, e6, e7) match {
      case Nil => Right(e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get)
      case f : List[L] =>  Left(f)
    }
  }

  def result[L,R1,R2,R3,R4,R5,R6,R7,R8](e1: Either[L,R1], e2: Either[L,R2], e3: Either[L,R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8]): Either[List[L], (R1,R2,R3,R4,R5,R6,R7,R8)] = {
    failures(e1, e2, e3, e4, e5, e6, e7, e8) match {
      case Nil => Right(e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get)
      case f : List[L] =>  Left(f)
    }
  }
  //TODO more parameters for results. use macro?

}
