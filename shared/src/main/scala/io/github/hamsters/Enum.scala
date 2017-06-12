package io.github.hamsters

trait Enumerable[A] {

  def name(a: A): String = a.toString.toLowerCase

  def parse(s: String): Option[A] = list.find(a => name(a) == s)

  def list: List[A]
}

object Enumeration {

  def name[A](a: A)(implicit ev: Enumerable[_ >: A]): String = ev.name(a)

  def parse[A](s: String)(implicit ev: Enumerable[_ <: A]): Option[A] = ev.parse(s)

  def list[A](implicit ev: Enumerable[A]): List[A] = ev.list
}
