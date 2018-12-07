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

  /** Return successes (right) for several Either values
    * @param eithers
    * @return successes
    */
  def successes[R](eithers : Either[_, R]*) : List[R]= {
    eithers.toList.collect {
      case r : Right[_, R] => r.right.get
    }
  }

  /**
    * Tells if eithers contain successes (right)
    * @param eithers
    * @tparam R
    * @return boolean
    */
  def hasSuccesses[R](eithers: Either[_, R]*): Boolean = successes(eithers: _*).nonEmpty


  /**
    * Retrieve failures (left) for several Either values
    * @param eithers
    * @tparam L
    * @return failures
    */
  def failures[L](eithers: Either[L, _]*): List[L] = eithers.toList.collect { case l: Left[L, _] => l.left.get }

  /**
    * Tells if eithers contain failures (left)
    * @param eithers
    * @tparam L
    * @return boolean
    */
  def hasFailures[L](eithers: Either[L, _]*): Boolean = failures(eithers: _*).nonEmpty

}
