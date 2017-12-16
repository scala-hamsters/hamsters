package io.github.hamsters

import scala.language.higherKinds

trait Functor[Box[_]] {

  def map[In, Out](boxA: Box[In])(f: In => Out): Box[Out]

}

object Functor {
  implicit val functorOption : Functor[Option] = new Functor[Option] {
    override def map[In, Out](boxA: Option[In])(f: In => Out) = boxA.map(f)
  }

  implicit val functorList : Functor[List] = new Functor[List] {
    override def map[In, Out](boxA: List[In])(f: In => Out) = boxA.map(f)
  }

}