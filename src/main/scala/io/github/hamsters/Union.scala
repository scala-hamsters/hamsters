package io.github.hamsters


//TODO 6 to n arity (n to be defined). Generate via macros?

case class Union2[T1, T2](v1: Option[T1], v2: Option[T2])

case class Union3[T1, T2, T3](v1: Option[T1], v2: Option[T2], v3: Option[T3])

case class Union4[T1, T2, T3, T4](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4])

case class Union5[T1, T2, T3, T4, T5](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4], v5: Option[T5])

class Union2Type[T1, T2] {
  implicit def toUnion21(t: T1) = new Union2[T1, T2](Some(t), None)

  implicit def toUnion22(t: T2) = new Union2[T1, T2](None, Some(t))
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

