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
    * Arity 2 to 22 methods are available
    * @param e1 : first either
    * @param e2 : second either
    * @tparam L
    * @tparam R1
    * @tparam R2
    * @return a tuple (R1, R2) of results in a Right if validation is successful or a list of errors L if it fails
    */
  def run[L, R1, R2](e1: Either[L, R1], e2: Either[L, R2]): Either[List[L], (R1, R2)] =
    (e1, e2) match {
      case (Right(r1), Right(r2)) => Right((r1, r2))
      case _ => Left(failures(e1, e2))
    }

  /**
    * Run the validation
    * This validation accumulates all errors
    * Arity 2 to 22 methods are available
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
    (e1, e2, e3) match {
      case (Right(r1), Right(r2), Right(r3)) => Right((r1, r2, r3))
      case _ => Left(failures(e1, e2, e3))
    }

  def run[L, R1, R2, R3, R4](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4]): Either[List[L], (R1, R2, R3, R4)] =
    (e1, e2, e3, e4) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4)) => Right((r1, r2, r3, r4))
      case _ => Left(failures(e1, e2, e3, e4))
    }

  def run[L, R1, R2, R3, R4, R5](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5]): Either[List[L], (R1, R2, R3, R4, R5)] =
    (e1, e2, e3, e4, e5) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5)) => Right((r1, r2, r3, r4, r5))
      case _ => Left(failures(e1, e2, e3, e4, e5))
    }

  def run[L, R1, R2, R3, R4, R5, R6](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6]): Either[List[L], (R1, R2, R3, R4, R5, R6)] =
    (e1, e2, e3, e4, e5, e6) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6)) => Right((r1, r2, r3, r4, r5, r6))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7)] =
    (e1, e2, e3, e4, e5, e6, e7) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7)) => Right((r1, r2, r3, r4, r5, r6, r7))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8)] =
    (e1, e2, e3, e4, e5, e6, e7, e8) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8)) => Right((r1, r2, r3, r4, r5, r6, r7, r8))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17), Right(r18)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17), Right(r18), Right(r19)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17), Right(r18), Right(r19), Right(r20)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20], e21: Either[L, R21]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17), Right(r18), Right(r19), Right(r20), Right(r21)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21))
    }

  def run[L, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21, R22](e1: Either[L, R1], e2: Either[L, R2], e3: Either[L, R3], e4: Either[L, R4], e5: Either[L, R5], e6: Either[L, R6], e7: Either[L, R7], e8: Either[L, R8], e9: Either[L, R9], e10: Either[L, R10], e11: Either[L, R11], e12: Either[L, R12], e13: Either[L, R13], e14: Either[L, R14], e15: Either[L, R15], e16: Either[L, R16], e17: Either[L, R17], e18: Either[L, R18], e19: Either[L, R19], e20: Either[L, R20], e21: Either[L, R21], e22: Either[L, R22]): Either[List[L], (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21, R22)] =
    (e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22) match {
      case (Right(r1), Right(r2), Right(r3), Right(r4), Right(r5), Right(r6), Right(r7), Right(r8), Right(r9), Right(r10), Right(r11), Right(r12), Right(r13), Right(r14), Right(r15), Right(r16), Right(r17), Right(r18), Right(r19), Right(r20), Right(r21), Right(r22)) => Right((r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22))
      case _ => Left(failures(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22))
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

}
