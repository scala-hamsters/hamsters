package io.github.hamsters

import scala.util.{Left, Right, Try, Success, Failure}

object Validation {

  implicit def fromTry[T](t: Try[T]): Either[String, T] = t match {
    case Success(res) => Right(res)
    case Failure(e) => Left(e.getMessage)
  }


  val Valid = Right
  val Invalid = Left

  /**
    * Return Either from code block
    * @param body
    * @tparam R
    * @return Either from code block
    */
  def fromCatchable[R](body: => R): Either[String, R] = {
    val throwableToErrorMessage = (t: Throwable) => Option(t.getMessage).getOrElse("Error: an exception has been thrown")
    fromCatchable(body, throwableToErrorMessage)
  }

  /**
    * Return Either from code block with custom error management
    * @param body
    * @param errorMgt : error management function
    * @tparam L
    * @tparam R
    * @return Either from code block
    */
  def fromCatchable[L, R](body: => R, errorMgt: Throwable => L): Either[L, R] =
    try {
      Right(body)
    } catch {
      case t: Throwable => Left(errorMgt(t))
    }

  /** Return successes (right) for several Either values
    * Validation.run should be used instead in most cases as it is a type-safer method (it does not return a List[Any])
    * @param eithers
    * @return successes
    */
  def successes(eithers : Either[_, _]*) : List[Any] = eithers.toList.collect { case r : Right[_, _] => r.value }

  /**
    * Tells if eithers contain successes (right)
    * @param eithers
    * @tparam R
    * @return boolean
    */
  def hasSuccesses[R](eithers: Either[_, R]*): Boolean = successes(eithers: _*).nonEmpty

  /**
    * Run the validation
    * This validation accumulates all errors
    * Arity 2 to 22 methods are available and generated from macro
    * @param e1 : first either
    * @param e2 : second either
    * @tparam L
    * @tparam R1
    * @tparam R2
    * @return a tuple (R1, R2) of results in a Right if validation is successful or a list of errors L if it fails
    */
  def run[L, R1, R2](e1: Either[L, R1], e2: Either[L, R2]): Either[List[L], (R1, R2)] =
    failures(e1, e2) match {
      case Nil => Right((e1.get, e2.get))
      case f: List[L] => Left(f)
    }

  /**
    * Run the validation
    * This validation accumulates all errors
    * Arity 2 to 22 methods are available and generated from macro
    * @param e1 : first either
    * @param e2 : second either
    * @param e3 : third either
    * @tparam L
    * @tparam R1
    * @tparam R2
    * @tparam R3
    * @return a tuple (R1, R2, R3) of results in a Right if validation is successful or a list of errors L if it fails
    */
  def run[L, R1, R2, R3](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3]): Either[List[L], (R1, R2, R3)] =
    failures(e1, e2, e3) match {
      case Nil => Right((e1.get, e2.get, e3.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4]): Either[List[L], (R1, R2, R3, R4)] =
    failures(e1, e2, e3, e4) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5]): Either[List[L], (R1, R2, R3, R4, R5)] =
    failures(e1, e2, e3, e4, e5) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6]): Either[List[L], (R1, R2, R3, R4, R5, R6)] =
    failures(e1, e2, e3, e4, e5, e6) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7)] =
    failures(e1, e2, e3, e4, e5, e6, e7) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get, e18.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get, e18.get, e19.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get, e18.get, e19.get, e20.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20], e21: Either[L, R21]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get, e18.get, e19.get, e20.get, e21.get))
      case f: List[L] => Left(f)
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21, R22](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20], e21: Either[L, R21], e22: Either[L, R22]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21, R22)] =
    failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22) match {
      case Nil => Right((e1.get, e2.get, e3.get, e4.get, e5.get, e6.get, e7.get, e8.get, e9.get, e10.get, e11.get, e12.get, e13.get, e14.get, e15.get, e16.get, e17.get, e18.get, e19.get, e20.get, e21.get, e22.get))
      case f: List[L] => Left(f)
    }


  /**
    * Retrieve failures (left) for several Either values
    * @param eithers
    * @tparam L
    * @return failures
    */
  def failures[L](eithers: Either[L, _]*): List[L] = eithers.toList.collect { case l: Left[L, _] => l.value }

  /**
    * Tells if eithers contain failures (left)
    * @param eithers
    * @tparam L
    * @return boolean
    */
  def hasFailures[L](eithers: Either[L, _]*): Boolean = failures(eithers: _*).nonEmpty

  implicit class RightBiasedEither[L, R](e: Either[L, R]) {
    def map[R2](f: R => R2): Either[L, R2] = e.map(f)

    def flatMap[R2](f: R => Either[L, R2]): Either[L, R2] = e.flatMap(f)

    def filter(p: R => Boolean): Option[Either[Nothing, R]] = filterWith(p)

    def filterWith(p: R => Boolean): Option[Either[Nothing, R]] = e.toOption.filter(p).map(Right(_))

    def get: R = e.toOption.get

    def getOrElse[R2 >: R](or: => R2): R2 = e.getOrElse(or)
  }

}
