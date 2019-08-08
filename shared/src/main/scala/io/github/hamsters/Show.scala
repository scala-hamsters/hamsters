package io.github.hamsters

import java.util.Date

trait Showable[A] {
  def format(value: A): String
}

object Showable {
  implicit val stringShowable: Showable[String] = new Showable[String] {
    override def format(value: String): String = value
  }

  implicit val intShowable: Showable[Int] = new Showable[Int] {
    override def format(value: Int): String = value.toString
  }

  implicit val charShowable: Showable[Char] = new Showable[Char] {
    override def format(value: Char): String = value.toString
  }

  implicit val doubleShowable: Showable[Double] = new Showable[Double] {
    override def format(value: Double): String = value.toString
  }

  implicit val floatShowable: Showable[Float] = new Showable[Float] {
    override def format(value: Float): String = value.toString
  }

  implicit val longShowable: Showable[Long] = new Showable[Long] {
    override def format(value: Long): String = value.toString
  }

  implicit val jdkDateShowable: Showable[Date] = new Showable[Date] {
    override def format(value: Date): String = value.toString
  }

  implicit val booleanShowable: Showable[Boolean]   = new Showable[Boolean] {
    override def format(value: Boolean): String = value.toString
  }

  implicit val byteShowable: Showable[Byte] = new Showable[Byte] {
    override def format(value: Byte): String = value.toString
  }

  implicit val shortShowable: Showable[Short] = new Showable[Short] {
    override def format(value: Short): String = value.toString
  }

  implicit val unitShowable: Showable[Unit] = new Showable[Unit] {
    override def format(value: Unit): String = value.toString
  }

  implicit def showClass[T]: Showable[Class[T]] = new Showable[Class[T]] {
    override def format(value: Class[T]): String = value.getSimpleName.replace("$","")
  }}

object Show {
  def show[A](value: A)(implicit s: Showable[A]): String = s.format(value)
}

object ShowableSyntax {

  implicit class ShowableOps[A](val value: A) extends AnyVal {
    def show(implicit s: Showable[A]): String = s.format(value)
  }

}


