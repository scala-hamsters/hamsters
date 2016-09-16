package io.github.hamsters

import scala.reflect.runtime.universe._
import scala.reflect._
// Should we generate (via macros) 6 to n arity ?

private object Union {
  implicit class MyInstanceOf[U: TypeTag](that: U) {
    def myIsInstanceOf[T: TypeTag] =
      typeOf[U] <:< typeOf[T]
  }
}

case class Union2[T1, T2](v1: Option[T1], v2: Option[T2])(implicit m1: Manifest[T1], m2: Manifest[T2]) {
  def find[T](implicit m: Manifest[T]): Option[T] = {
    import Union._
    v1 match {
      case Some(u) if(u.myIsInstanceOf[T]) =>  Some(u).asInstanceOf[Some[T]]
      case _ => v2 match {
        case Some(u) if(u.myIsInstanceOf[T]) => Some(u).asInstanceOf[Some[T]]
        case _ => None
      }
    }
  }
}

case class Union3[T1, T2, T3](v1: Option[T1], v2: Option[T2], v3: Option[T3])

case class Union4[T1, T2, T3, T4](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4])

case class Union5[T1, T2, T3, T4, T5](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4], v5: Option[T5])

class Union2Type[T1, T2] {
  implicit def toUnion21(t: T1)(implicit m1: Manifest[T1], m2: Manifest[T2]) = new Union2[T1, T2](Some(t), None)

  implicit def toUnion22(t: T2)(implicit m1: Manifest[T1], m2: Manifest[T2]) = new Union2[T1, T2](None, Some(t))
}

class Union3Type[T1, T2, T3] {
  implicit def toUnion31(t: T1) = new Union3[T1, T2, T3](Some(t), None, None)

  implicit def toUnion32(t: T2) = new Union3[T1, T2, T3](None, Some(t), None)

  implicit def toUnion33(t: T3) = new Union3[T1, T2, T3](None, None, Some(t))
}

class Union4Type[T1, T2, T3, T4] {
  implicit def toUnion41(t: T1) = new Union4[T1, T2, T3, T4](Some(t), None, None, None)

  implicit def toUnion42(t: T2) = new Union4[T1, T2, T3, T4](None, Some(t), None, None)

  implicit def toUnion43(t: T3) = new Union4[T1, T2, T3, T4](None, None, Some(t), None)

  implicit def toUnion44(t: T4) = new Union4[T1, T2, T3, T4](None, None, None, Some(t))

}

class Union5Type[T1, T2, T3, T4, T5] {
  implicit def toUnion51(t: T1) = new Union5[T1, T2, T3, T4, T5](Some(t), None, None, None, None)

  implicit def toUnion52(t: T2) = new Union5[T1, T2, T3, T4, T5](None, Some(t), None, None, None)

  implicit def toUnion53(t: T3) = new Union5[T1, T2, T3, T4, T5](None, None, Some(t), None, None)

  implicit def toUnion54(t: T4) = new Union5[T1, T2, T3, T4, T5](None, None, None, Some(t), None)

  implicit def toUnion55(t: T5) = new Union5[T1, T2, T3, T4, T5](None, None, None, None, Some(t))
}
