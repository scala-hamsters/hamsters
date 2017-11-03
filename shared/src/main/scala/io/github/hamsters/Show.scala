package io.github.hamsters

import java.time.{LocalDate, LocalDateTime}
import java.util.Date

trait Showable[A] {
  def format(value: A): String
}

object Show {
  def show[A](value: A)(implicit s: Showable[A]): String = s.format(value)
}

object ShowableSyntax {

  implicit val stringShowable: Showable[String] = new Showable[String] {
    override def format(value: String) = value
  }

  implicit val intShowable: Showable[Int] = new Showable[Int] {
    override def format(value: Int) = value.toString
  }

  implicit val doubleShowable: Showable[Double] = new Showable[Double] {
    override def format(value: Double) = value.toString
  }

  implicit val floatShowable: Showable[Float] = new Showable[Float] {
    override def format(value: Float) = value.toString
  }

  implicit val longShowable: Showable[Long] = new Showable[Long] {
    override def format(value: Long) = value.toString
  }

  implicit val jdkDateShowable: Showable[Date] = new Showable[Date] {
    override def format(value: Date) = value.toString
  }

  implicit val jdkLocalDateShowable: Showable[LocalDate] = new Showable[LocalDate] {
    override def format(value: LocalDate) = value.toString
  }

  implicit val jdkLocalDateTimeShowable: Showable[LocalDateTime] = new Showable[LocalDateTime] {
    override def format(value: LocalDateTime) = value.toString
  }

  implicit val booleanShowable: Showable[Boolean]   = new Showable[Boolean] {
    override def format(value: Boolean) = value.toString
  }

  implicit val byteShowable: Showable[Byte] = new Showable[Byte] {
    override def format(value: Byte) = value.toString
  }

  implicit val shortShowable: Showable[Short] = new Showable[Short] {
    override def format(value: Short) = value.toString
  }

  implicit val unitShowable: Showable[Unit] = new Showable[Unit] {
    override def format(value: Unit) = value.toString
  }

  implicit class ShowableOps[A](val value: A) extends AnyVal {
    def show(implicit s: Showable[A]): String = s.format(value)
  }

}



@ShowMacro
case class Name(firstName: String, lastName: String)

@ShowMacro
case class Person(name : Name, age: Int)