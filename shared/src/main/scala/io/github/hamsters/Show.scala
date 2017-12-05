package io.github.hamsters

import java.util.Date

trait Showable[A] {
  def format(value: A): String
}

object Showable {
  implicit val stringShowable: Showable[String] = (value: String) => value

  implicit val intShowable: Showable[Int] = (value: Int) => value.toString

  implicit val charShowable: Showable[Char] = (value: Char) => value.toString

  implicit val doubleShowable: Showable[Double] = (value: Double) => value.toString

  implicit val floatShowable: Showable[Float] = (value: Float) => value.toString

  implicit val longShowable: Showable[Long] = (value: Long) => value.toString

  implicit val jdkDateShowable: Showable[Date] = (value: Date) => value.toString

  implicit val booleanShowable: Showable[Boolean]   = (value: Boolean) => value.toString

  implicit val byteShowable: Showable[Byte] = (value: Byte) => value.toString

  implicit val shortShowable: Showable[Short] = (value: Short) => value.toString

  implicit val unitShowable: Showable[Unit] = (value: Unit) => value.toString

  implicit def showClass[T] = new Showable[Class[T]] {
    override def format(value: Class[T]) = value.getSimpleName.replace("$","")
  }}

object Show {
  def show[A](value: A)(implicit s: Showable[A]): String = s.format(value)
}

object ShowableSyntax {

  implicit class ShowableOps[A](val value: A) extends AnyVal {
    def show(implicit s: Showable[A]): String = s.format(value)
  }

}


