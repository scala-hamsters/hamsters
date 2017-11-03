package io.github.hamsters

case class NonEmptyList[A](head: A, tail: List[A]) {
  def toList(): List[A] = head :: tail
}
object NonEmptyList {
  def apply[A](head: A, tail: A*): NonEmptyList[A] = {
    NonEmptyList(head, tail.toList)
  }
}
