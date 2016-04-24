package io.github.hamsters

import scala.annotation.tailrec

trait HList {
  def ::[U](v: U): HList
}

case class HCons[T, U <: HList](head : T, tail : U) extends HList {

  override def ::[V](v: V) = HCons(v, this)

  def foldLeft[V](zero: V)(f: (V, Any) => V): V = {
    @tailrec
    def foldLeft0(accu: V, head: Any, tail: HList): V = {
      val newAccu = f(accu, head)
      tail match {
        case HNil => newAccu
        case t: HCons[_,_] => foldLeft0(newAccu, t.head, t.tail)
      }
    }
    foldLeft0(zero, head, tail)
  }


  def map[U](f: (Any) => Any): HList = {

    def map0(accu: HList, rest: HList): HList = {

      rest match {
        case r: HNil => accu
        case r: HCons[_,_] => accu match {
          case HNil => map0(f(r.head) :: HNil, r.tail)
          case a: HCons[_,_] => map0(a ++ (f(r.head) :: HNil), r.tail)
        }

      }
    }

    map0(HNil, this)
  }

  def filter(p: (Any) => Boolean): HList = {

    def filter0(accu: HList, rest: HList): HList = {
      rest match {
        case HNil => accu
        case r: HCons[_,_] => accu match {
          case HNil => if (p(r.head)) filter0(r.head :: HNil, r.tail) else filter0(accu, r.tail)
          case a: HCons[_,_] => if (p(r.head)) filter0(a ++ (r.head :: HNil), r.tail) else filter0(accu, r.tail)}
        }
    }
    filter0(HNil, this)
  }

  def ++[V <: HList](l2: V): HCons[T,U] = {
    def append[X <: HList, Y <: HList](l1: HCons[T,X], l2: Y): HCons[T,_] = {
      l1.tail match {
        case HNil => HCons(l1.head, l2)
        case t: HCons[T,U] => l1.head :: append(t, l2)
      }
    }
    append(this, l2).asInstanceOf[HCons[T,U]]
  }


  def foreach(f: (Any) => Unit): Unit = map(f)

  override def toString = s"($head :: $tail)"

}


class HNil extends HList {
  override def ::[T](v: T) = HCons(v, this)

  override def toString = "HNil"
}

object HNil extends HNil
