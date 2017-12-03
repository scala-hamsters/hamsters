package io.github.hamsters

import scala.annotation.tailrec
import scala.reflect.ClassTag

sealed trait HList {

  type Plus[L <: HList] <: HList

  /**
   * Retrieve element at index
   *
   * @param index
   * @tparam A
   * @return element at index
   */
  def apply[A: ClassTag](index: Int): A

  /**
   * Retrieve element at index in an Option
   *
   * @param index
   * @tparam A
   * @return Option of element at index
   */
  def get[A: ClassTag](index: Int): Option[A]

  /**
   * Append element at the beginning of this HList
   *
   * @param t
   * @tparam T
   * @return a new HList
   */
  def ::[T](t: T): HList

  /**
   * Filter this HList with a predicate
   *
   * @param p
   * @return a new filtered HList
   */
  def filter(p: (Any) => Boolean): HList

  /**
   * Apply f to all elements of this HList
   *
   * @param f
   * @return a new HList
   */
  def map(f: (Any) => Any): HList

  /**
   * Executes f for each element of the HList
   *
   * @param f
   */
  def foreach(f: (Any) => Unit): Unit = map(f)

  override def toString: String

}

class HNil extends HList {

  type Plus[L <: HList] = L

  override def apply[A: ClassTag](index: Int) = throw new Exception("empty Hlist")

  /**
   * Retrieve element at index in an Option
   *
   * @param index
   * @tparam A
   * @return Option of element at index
   */
  override def get[A: ClassTag](index: Int) = None

  /**
   * Append element at the beginning of this HList
   *
   * @param h
   * @tparam H
   * @return a new HList
   */
  override def ::[H](h: H) = HCons(h, this)

  /**
   * Filter this HList with a predicate
   *
   * @param p
   * @return HNil
   */
  override def filter(p: (Any) => Boolean) = HNil

  /**
   * Apply f to all elements
   *
   * @param f
   * @return HNil
   */
  override def map(f: (Any) => Any) = HNil

  override def toString = "HNIL"

}

object HNil extends HNil

case class Appender[L1 <: HList, L2 <: HList, R <: HList](f: (L1, L2) => R) {
  def apply(l1: L1, l2: L2) = f(l1, l2)
}

case class HCons[H, T <: HList](head: H, tail: T) extends HList {

  type Plus[L <: HList] = HCons[H, T#Plus[L]]

  /**
   * Retrieve element at index
   *
   * @param index
   * @tparam A
   * @return element at index
   */
  override def apply[A: ClassTag](index: Int) = get[A](index).getOrElse(throw new Exception("Index not found for this type"))

  /**
   * Retrieve element at index in an Option
   *
   * @param index
   * @tparam A
   * @return Option of element at index
   */
  override def get[A: ClassTag](index: Int) = {
    def get(head: Any, tail: HList, index: Int): Option[A] = {
      if (index > 0) {
        tail match {
          case _: HNil => None
          case h: HCons[_, _] => get(h.head, h.tail, index - 1)
        }
      }
      else {
        head match {
          case value: A => Some(value)
          case _ => None
        }
      }
    }

    get(head, tail, index)
  }

  /**
   * Append element at the beginning of this HList
   *
   * @param a
   * @tparam A
   * @return a new HList
   */
  override def ::[A](a: A) = HCons(a, this)

  /**
   * Append another HList to this HList
   * @param l2
   * @param f
   * @tparam L2
   * @return a new HList
   */
  def ++[L2 <: HList](l2: L2)(implicit f: Appender[HCons[H, T], L2, Plus[L2]]): HCons[H, T#Plus[L2]] = HList.++(this, l2)

  /**
   * Append an element to this HList
   * @param a
   * @param f
   * @tparam A
   * @return a new HList
   */
  def +[A](a: A)(implicit f: Appender[HCons[H, T], HCons[A, HNil], Plus[HCons[A, HNil]]]): HCons[H, T#Plus[HCons[A, HNil]]] = HList.++(this, a :: HNil)

  private def +++(l2: HList) = {
    def append(l1: HCons[H, _], l2: HList): HCons[H, _] = {
      l1.tail match {
        case HNil => HCons(l1.head, l2)
        case h: HCons[H, _] => l1.head :: append(h, l2)
      }
    }

    append(this, l2)
  }

  /**
   * Applies a binary operator to a start value and all elements of this HList, going left to right.
   * @param zero
   * @param f
   * @tparam A
   * @return computed result
   */
  def foldLeft[A](zero: A)(f: (A, Any) => A): A = {
    @tailrec
    def foldLeft0(accu: A, head: Any, tail: HList): A = {
      val newAccu = f(accu, head)
      tail match {
        case _: HNil => newAccu
        case h: HCons[_, _] => foldLeft0(newAccu, h.head, h.tail)
      }
    }

    foldLeft0(zero, head, tail)
  }

  /**
   * Filter this HList with a predicate
   *
   * @param p
   * @return HNil
   */
  override def filter(p: (Any) => Boolean): HList = {
    @tailrec
    def filter0(accu: HList, rest: HList): HList = {
      rest match {
        case _: HNil => accu
        case r: HCons[_, _] => accu match {
          case _: HNil => if (p(r.head)) filter0(r.head :: HNil, r.tail) else filter0(accu, r.tail)
          case a: HCons[_, _] => if (p(r.head)) filter0(a +++ (r.head :: HNil), r.tail) else filter0(accu, r.tail)
        }
      }
    }

    filter0(HNil, this)
  }

  /**
   * Apply f to all elements of this HList
   *
   * @param f
   * @return a new HList
   */
  def map(f: (Any) => Any): HList = {

    @tailrec
    def map0(accu: HList, rest: HList): HList = {
      rest match {
        case _: HNil => accu
        case r: HCons[_, _] => accu match {
          case _: HNil => map0(f(r.head) :: HNil, r.tail)
          case a: HCons[_, _] => map0(a +++ (f(r.head) :: HNil), r.tail)
        }
      }
    }

    map0(HNil, this)
  }

  override def toString = s"($head : $tail)"

}

object HList {

  type ::[H,T <: HList] = HCons[H,T]


  def ++[L1 <: HList, L2 <: HList](l1: L1, l2: L2)(implicit f: Appender[L1, L2, L1#Plus[L2]]): L1#Plus[L2] = f(l1, l2)

  def toHList[T]( value : T)(implicit toHlist : T => HList) : HList = toHlist(value)

  def toClass[T](value : HList)(implicit toClass : HList => T) : T = toClass(value)


  // inspired by http://jnordenberg.blogspot.fr/2008/08/hlist-in-scala.html  
  implicit def nilAppender[L <: HList] = Appender((v: HNil, l: L) => l)

  implicit def consAppender[T, L1 <: HList, L2 <: HList, R <: HList](implicit f: Appender[L1, L2, R]) = {
    Appender[HCons[T, L1], L2, HCons[T, R]]((l1: HCons[T, L1], l2: L2) => HCons(l1.head, f(l1.tail, l2)))
  }

}
