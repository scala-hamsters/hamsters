package io.github.hamsters

import scala.util.{Left, Right}

object EitherOps {

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

  /**
   * Retrieves Left values for several Either values
   */
  def collectLefts[L](eithers: Either[L,_]*): Seq[L] = eithers.collect { 
    case Left(l) => l
  }


  /**
   * Retrieves Right values for several Either values
   */
  def collectRights[R](eithers: Either[_,R]*): Seq[R] = eithers.collect { 
    case Right(r) => r
  }

}