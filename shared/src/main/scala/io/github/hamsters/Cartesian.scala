package io.github.hamsters

import scala.language.implicitConversions
import scala.concurrent.{ExecutionContext, Future}

trait Cartesian[Box[_]] {
  def product[A, B](a: Box[A], b: Box[B]): Box[(A, B)]
}

object Cartesian {

  implicit def cartesianOption : Cartesian[Option] = new Cartesian[Option] {
    override def product[A, B](maybeA: Option[A], maybeB: Option[B]): Option[(A, B)] = (maybeA, maybeB) match {
      case (Some(a), Some(b)) => Some(a -> b)
      case _ => None
    }
  }

  implicit def cartesianList:Cartesian[List] = new Cartesian[List] {
    override def product[A, B](listA: List[A], listB: List[B]): List[(A, B)] = for {
      a <- listA
      b <- listB
    } yield a -> b
  }

  implicit def cartesianFuture(implicit ec: ExecutionContext): Cartesian[Future] = new Cartesian[Future] {
    override def product[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] = for {
      a <- futureA
      b <- futureB
    } yield a -> b
  }

  case class Tuple2Box[Box[_], T](t: (Box[T], Box[T])) {
    def mapN(f: (T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(t._1, t._2)) { case (x1, x2) => f(x1, x2) }
  }
  implicit def t2x[Box[_], T](t: (Box[T], Box[T])): Tuple2Box[Box, T] = Tuple2Box(t)

  case class Tuple3Box[Box[_], T](t: (Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(t._1, t._2), t._3)) { case ((x1, x2), x3) => f(x1, x2, x3) }
  }
  implicit def t3x[Box[_], T](t: (Box[T], Box[T], Box[T])): Tuple3Box[Box, T] = Tuple3Box(t)

  case class Tuple4Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(t._1, t._2), t._3), t._4)) { case (((x1, x2), x3), x4) => f(x1, x2, x3, x4) }
  }
  implicit def t4x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T])): Tuple4Box[Box, T] = Tuple4Box(t)

  case class Tuple5Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5)) { case ((((x1, x2), x3), x4), x5) => f(x1, x2, x3, x4, x5) }
  }
  implicit def t5x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple5Box[Box, T] = Tuple5Box(t)

  case class Tuple6Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6)) { case (((((x1, x2), x3), x4), x5), x6) => f(x1, x2, x3, x4, x5, x6) }
  }
  implicit def t6x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple6Box[Box, T] = Tuple6Box(t)

  case class Tuple7Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7)) { case ((((((x1, x2), x3), x4), x5), x6), x7) => f(x1, x2, x3, x4, x5, x6, x7) }
  }
  implicit def t7x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple7Box[Box, T] = Tuple7Box(t)

  case class Tuple8Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8)) { case (((((((x1, x2), x3), x4), x5), x6), x7), x8) => f(x1, x2, x3, x4, x5, x6, x7, x8) }
  }
  implicit def t8x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple8Box[Box, T] = Tuple8Box(t)

  case class Tuple9Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9)) { case ((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9) }
  }
  implicit def t9x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple9Box[Box, T] = Tuple9Box(t)

  case class Tuple10Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10)) { case (((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10) }
  }
  implicit def t10x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple10Box[Box, T] = Tuple10Box(t)

  case class Tuple11Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11)) { case ((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11) }
  }
  implicit def t11x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple11Box[Box, T] = Tuple11Box(t)

  case class Tuple12Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12)) { case (((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12) }
  }
  implicit def t12x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple12Box[Box, T] = Tuple12Box(t)

  case class Tuple13Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13)) { case ((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13) }
  }
  implicit def t13x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple13Box[Box, T] = Tuple13Box(t)

  case class Tuple14Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14)) { case (((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14) }
  }
  implicit def t14x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple14Box[Box, T] = Tuple14Box(t)

  case class Tuple15Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15)) { case ((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15) }
  }
  implicit def t15x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple15Box[Box, T] = Tuple15Box(t)

  case class Tuple16Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16)) { case (((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16) }
  }
  implicit def t16x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple16Box[Box, T] = Tuple16Box(t)

  case class Tuple17Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17)) { case ((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17) }
  }
  implicit def t17x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple17Box[Box, T] = Tuple17Box(t)

  case class Tuple18Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17), t._18)) { case (((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17), x18) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18) }
  }
  implicit def t18x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple18Box[Box, T] = Tuple18Box(t)

  case class Tuple19Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17), t._18), t._19)) { case ((((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17), x18), x19) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19) }
  }
  implicit def t19x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple19Box[Box, T] = Tuple19Box(t)

  case class Tuple20Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17), t._18), t._19), t._20)) { case (((((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17), x18), x19), x20) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20) }
  }
  implicit def t20x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple20Box[Box, T] = Tuple20Box(t)

  case class Tuple21Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17), t._18), t._19), t._20), t._21)) { case ((((((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17), x18), x19), x20), x21) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20, x21) }
  }
  implicit def t21x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple21Box[Box, T] = Tuple21Box(t)

  case class Tuple22Box[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])) {
    def mapN(f: (T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T) => T)(implicit functor: Functor[Box], s: Cartesian[Box]): Box[T] =
      functor.map(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(s.product(t._1, t._2), t._3), t._4), t._5), t._6), t._7), t._8), t._9), t._10), t._11), t._12), t._13), t._14), t._15), t._16), t._17), t._18), t._19), t._20), t._21), t._22)) { case (((((((((((((((((((((x1, x2), x3), x4), x5), x6), x7), x8), x9), x10), x11), x12), x13), x14), x15), x16), x17), x18), x19), x20), x21), x22) => f(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20, x21, x22) }
  }
  implicit def t22x[Box[_], T](t: (Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T], Box[T])): Tuple22Box[Box, T] = Tuple22Box(t)

}
