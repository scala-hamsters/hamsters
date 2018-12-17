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

  implicit class EitherLeftOps[L](eithers: Seq[Either[L,_]]){
     
    /**
     * Retrieves Left values for several Either values
     */
    def collectLefts: Seq[L] = eithers.collect { 
      case Left(l) => l
    }
  }

  implicit class EitherRightOps[R](eithers: Seq[Either[_,R]]){
    
    /**
     * Retrieves Right values for several Either values
     */
    def collectRights: Seq[R] = eithers.collect { 
      case Right(r) => r
    }
  }

}