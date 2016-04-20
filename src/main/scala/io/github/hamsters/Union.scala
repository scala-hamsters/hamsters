package io.github.hamsters

case class Union2[T1,T2](v1: Option[T1], v2: Option[T2])
case class Union3[T1,T2,T3](v1: Option[T1], v2: Option[T2], v3: Option[T3])

class Union2Type[T1,T2]{
  implicit def toUnion21(t: T1) = new Union2[T1,T2](Some(t), None)
  implicit def toUnion22(t: T2) = new Union2[T1,T2](None, Some(t))
}

class Union3Type[T1,T2,T3]{
  implicit def toUnion31(t: T1) = new Union3[T1,T2,T3](Some(t), None, None)
  implicit def toUnion32(t: T2) = new Union3[T1,T2,T3](None, Some(t), None)
  implicit def toUnion33(t: T3) = new Union3[T1,T2,T3](None, None, Some(t))
}

// TODO 4 to n arity (n to be defined)
// generate via macros?