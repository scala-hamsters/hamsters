package io.github.hamsters

trait Showable[A] {
  def format(value: A): String
}

object Show {
  def show[A](value: A)(implicit s: Showable[A]): String = s.format(value)
}

object ShowableSyntax {

  implicit val stringShowable = new Showable[String] {
    override def format(value: String) = value
  }

  implicit val intShowable = new Showable[Int] {
    override def format(value: Int) = value.toString
  }

  implicit class ShowtOps[A](value: A) {
    def show(implicit s: Showable[A]): String = s.format(value)
  }

}



@ShowMacro
case class Name(firstName: String, lastName: String)

@ShowMacro
case class Person(name : Name, age: Int)