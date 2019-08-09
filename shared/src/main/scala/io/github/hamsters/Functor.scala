package io.github.hamsters

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait Functor[Box[_]] {

  def map[In, Out](boxA: Box[In])(f: In => Out): Box[Out]

}

object Functor {
  implicit val functorOption : Functor[Option] = new Functor[Option] {
    override def map[In, Out](boxIn: Option[In])(f: In => Out): Option[Out] = boxIn.map(f)
  }

  implicit val functorList : Functor[List] = new Functor[List] {
    override def map[In, Out](boxIn: List[In])(f: In => Out): List[Out] = boxIn.map(f)
  }

  implicit def functorFuture(implicit ec: ExecutionContext) : Functor[Future] = new Functor[Future] {
    override def map[In, Out](boxIn: Future[In])(f: In => Out): Future[Out] = for {
      in <- boxIn
    } yield  f(in)
  }

}
