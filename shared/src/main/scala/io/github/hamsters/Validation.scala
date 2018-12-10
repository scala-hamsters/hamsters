package io.github.hamsters

import scala.util.{Left, Right}

@ValidationMacro
object Validation {

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
  def fromCatchable[L, R](body: => R, errorMgt: (Throwable) => L): Either[L, R] = {
    try {
      Right(body)
    }
    catch {
      case t: Throwable => Left(errorMgt(t))
    }
  }

 
  /** Retrieves Right values for several Either values
   */
  def successes[B](eithers : Either[_, B]*) : Seq[B]=  eithers.collect {
    case Right(b) => b
  }

  /**
    * Tells if eithers contain successes (right)
    * @param eithers
    * @tparam R
    * @return boolean
    */
  def hasSuccesses[R](eithers: Either[_, R]*): Boolean = successes(eithers: _*).nonEmpty


  /**
   * Retrieves Left values for several Either values
   */
  def failures[A](eithers: Either[A, _]*): Seq[A] = eithers.collect { 
    case Left(a) => a
  }

  /**
    * Tells if eithers contain failures (left)
    * @param eithers
    * @tparam L
    * @return boolean
    */
  def hasFailures[L](eithers: Either[L, _]*): Boolean = failures(eithers: _*).nonEmpty

}
