package io.github.hamsters

//TODO remove this case class, just here for the draft
case class Person(name: String, age: Int)

trait Showable[A] {
  def format(value: A): String
}

object Show {

  def show[A](value: A)(implicit s: Showable[A]): String = s.format(value)

  def print[A](value: A)(implicit s: Showable[A]): Unit = println(s.format(value))

  def format[A]: Showable[A] = {
    //TODO replace this by a macro to find the fields names for any type A
    new Showable[A] { 
      override def format(a: A) = a match {
        case Person(name, age) => s"Person(name = $name, age = $age)"   
      }
    }
  }

}

object ShowableSyntax {
  implicit class ShowtOps[A](value: A) {

    def show(implicit s: Showable[A]): String = s.format(value)

    def print(implicit s: Showable[A]): Unit = println(s.format(value))

  }
}

