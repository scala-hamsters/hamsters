package io.github.hamsters

import scala.annotation.tailrec

trait HList {
  def ::[T](v: T): HList

  //TODO def map
  //TODO def flatMap
  //TODO def filter
  //TODO def tupled
}

case class HCons[+T](head: T, tail: HList) extends HList {

  override def ::[U](v: U) = HCons(v, this)

  def foldLeft[U](zero: U)(f: (U, Any) => U): U = {
    @tailrec
    def foldLeft0(accu: U, head: Any, tail: HList): U = {
      val newAccu = f(accu, head)
      tail match {
        case t: HNil => newAccu
        case t: HCons[T] =>  foldLeft0(newAccu, t.head, t.tail)
      }
    }
    foldLeft0(zero, head, tail)
  }


  def map[U](f: (Any) => Any): HCons[Any] = {

    def map0(accu: HList, rest: HList): HCons[Any] = {
      accu match {
        case a: HNil => map0(f(head) :: HNil, tail)
        case a: HCons[_] => rest match {
          case r: HNil => a
          case r: HCons[_] => map0(a ++ (f(r.head) :: HNil), r.tail)
        }
      }


    }
    map0(HNil, this)
  }

  def filter(p: (Any) => Boolean): HCons[Any] = {
    //TODO
    0 :: HNil
  }

  def ++(l2: HList): HCons[T] = {
    //TODO @tailrec ?
    def append(l1: HCons[T], l2: HList): HCons[T] = {
      l1.tail match {
        case t: HNil => HCons(l1.head, l2)
        case t: HCons[T] => l1.head :: append(t, l2)
      }
    }
    append(this, l2)
  }

  override def toString = s"($head : $tail)"

}


class HNil extends HList {
  override def ::[T](v: T) = HCons(v, this)

  override def toString = "HNil"
}

object HNil extends HNil
