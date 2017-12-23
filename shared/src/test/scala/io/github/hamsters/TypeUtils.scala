package io.github.hamsters

trait TypeUtils {
  type A = Int
  type B = Double
  type C = String
  type D = Int

  val f: A => B = _.toDouble
  val g: B => C = _.toString
  val h: C => D = _.length

  val fg: A => C = f andThen g
  val gh: B => D = g andThen h
}
