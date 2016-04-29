//inspired by http://jnordenberg.blogspot.fr/2008/08/hlist-in-scala.html
sealed trait HList {
  type Plus[L <: HList] <: HList
  def ::[U](v: U): HList
}
case class Appender[L1 <: HList, L2 <: HList, R <: HList](fn: (L1, L2) => R) {
  def apply(l1: L1, l2: L2) = fn(l1, l2)
}
case class HCons[T, U <: HList](head : T, tail : U) extends HList {
  type Plus[L <: HList] = HCons[T, U#Plus[L]]

  def ::[V](v : V) = HCons(v, this)
}
class HNil extends HList {
  type Plus[L <: HList] = L
  def ::[T](v : T) = HCons(v, this)
}
case object HNil extends HNil
object HList{
def ++[L1 <: HList, L2 <: HList](l1 : L1, l2 : L2)
  (implicit f : Appender[L1, L2, L1#Plus[L2]]) : L1#Plus[L2] = f(l1, l2)
implicit def nilAppender[L <: HList] : Appender[HNil, L, L] =
  Appender((v : HNil, l : L) => l)
implicit def consAppender[T, L1 <: HList, L2 <: HList, R <: HList]
(implicit f : Appender[L1, L2, R]) : Appender[HCons[T, L1], L2, HCons[T, R]] =
  Appender[HCons[T, L1], L2, HCons[T, R]]((l1 : HCons[T, L1], l2 : L2) => HCons(l1.head, f(l1.tail, l2)))
}
import HList._
val l1 = 2.0 :: "hi" :: HNil
val l2 = 1 :: HNil
val sum = ++(l1,l2)
sum.tail.tail.head

//def ++[T2, T3, L2 <: HList, L3 <: HList](l1:HCons[T2, HCons[T3, HNil]] , l2: L2) = HList.append(l1, l2)
