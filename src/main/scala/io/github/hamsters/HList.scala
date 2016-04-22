package io.github.hamsters

trait HList {
  def ::[T](v: T): HList
}

case class HCons[T](head: T, tail: HList) extends HList {

  override def ::[U](v: U) = HCons(v, this)

  def ++(l2: HList): HCons[T] = {
    def append(l1: HCons[T], l2: HList): HCons[T] = {
      l1.tail match {
        case t: HNil => HCons(l1.head, l2)
        case t: HCons[T] => HCons(l1.head, append(t, l2))
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
