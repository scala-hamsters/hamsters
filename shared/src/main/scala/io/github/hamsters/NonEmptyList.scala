package io.github.hamsters

case class NonEmptyList[A](head: A, tail: List[A]) {

  /**
    * Convert a NonEmptyList to a List
    * @return a List
    */
  def toList: List[A] = head :: tail
}

object NonEmptyList {
  /**
    * Construct a non empty list
    * @param head
    * @param tail
    * @tparam A
    * @return a NonEmptyList
    */
  def apply[A](head: A, tail: A*): NonEmptyList[A] = NonEmptyList(head, tail.toList)
}
