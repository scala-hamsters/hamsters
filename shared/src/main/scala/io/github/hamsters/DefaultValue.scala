package io.github.hamsters

trait DefaultValue[A] {
  def get: A
}

object DefaultValue{

  implicit def stringDefaultValue = new DefaultValue[String] {
    /**
      * @return an empty String when an None of Option[String] is provided
      */
    override def get = ""
  }

  implicit def numericDefaultValue[T: Numeric] = new DefaultValue[T] {
    /**
      * @return zero an None of Option[Numeric] is provided
      */
    override def get = 0.asInstanceOf[T]
  }

  implicit def seqDefaultValue[T] = new DefaultValue[Seq[T]] {
    /**
      * @return an empty Seq when an None of Option[ Seq[T] ] is provided
      */
    override def get = Seq.empty[T]
  }

  implicit def listDefaultValue[T] = new DefaultValue[List[T]] {
    /**
      * @return an empty List when an None of Option[ Iterable[T] ] is provided
      */
    override def get = List.empty[T]
  }
  
}