package io.github.hamsters

object EmptyOptionValues {
  implicit class OrEmpty[A](optValue: Option[A])(implicit defaultValue: DefaultValue[A]) {
    /**
      *
      * @return an empty element of A as defined by DefaultValue companion object
      */
    def orEmpty: A = optValue.getOrElse(defaultValue.get)
  }
}