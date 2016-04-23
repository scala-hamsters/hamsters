package io.github.hamsters

import scala.annotation.tailrec

trait HList {
  def ::[U](v: U): HList

  def tail: HList

  def head: Any

  def ++(l2: HList): HList
}

case class HCons[+T](override val head: T, override val tail: HList) extends HList {

  override def ::[U](v: U) = HCons(v, this)

  def foldLeft[U](zero: U)(f: (U, Any) => U): U = {
    @tailrec
    def foldLeft0(accu: U, head: Any, tail: HList): U = {
      val newAccu = f(accu, head)
      tail match {
        case HNil => newAccu
        case t: HCons[T] => foldLeft0(newAccu, t.head, t.tail)
      }
    }
    foldLeft0(zero, head, tail)
  }


  def map[U](f: (Any) => Any): HList = {

    def map0(accu: HList, rest: HList): HList = {

      rest match {
        case r: HNil => accu
        case r: HCons[_] => map0(accu ++ (f(r.head) :: HNil), r.tail)
      }
    }

    map0(HNil, this)
  }

  def filter(p: (Any) => Boolean): HList = {

    def filter0(accu: HList, rest: HList): HList = {
      rest match {
        case HNil => accu
        case r: HCons[_] => if (p(r.head)) filter0(accu ++ (r.head :: HNil), r.tail) else filter0(accu, r.tail)}
    }
    filter0(HNil, this)
  }

  def ++(l2: HList): HCons[T] = {
    def append(l1: HCons[T], l2: HList): HCons[T] = {
      l1.tail match {
        case HNil => HCons(l1.head, l2)
        case t: HCons[T] => l1.head :: append(t, l2)
      }
    }
    append(this, l2)
  }

  override def toString = s"($head :: $tail)"

}


class HNil extends HList {
  override def ::[T](v: T) = HCons(v, this)

  override def toString = "HNil"

  override def tail = HNil

  override def head = throw new Exception("head of HNil")

  override def ++(l2: HList) = l2
}

object HNil extends HNil
