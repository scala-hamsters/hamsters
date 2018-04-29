import io.github.hamsters.{Cartesian, Functor}


import Cartesian._

val a1: Option[Int] = Some(1)
val a2: Option[Int] = Some(2)
val a3: Option[Int] = Some(3)

val b1: List[Int] = List(1)
val b2: List[Int] = List(2)
val b3: List[Int] = List(3)




(a1, a2, a3).mapN(_ + _ + _)
(b1, b2, b3).mapN(_ + _ + _)


