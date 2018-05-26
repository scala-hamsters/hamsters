package io.github.hamsters

import scala.reflect._

private object Union {
  implicit class NoEraseInstanceOf(that: Any) {
    def noEraseInstanceOf[T: ClassTag]= that match {
      case _: T => true
      case _ => false
    }
  }
}

trait Union { this: Product =>

  @scala.annotation.tailrec
  private def findTypeOnProductIterator[T : ClassTag](it: Iterator[Any]): Option[T] = {
    import Union._
    if(!it.hasNext) None
    else {
      val current = it.next()
      current match {
        case Some(u) if u.noEraseInstanceOf[T] => Some(u).asInstanceOf[Some[T]]
        case _ => findTypeOnProductIterator(it)
      }
    }
  }

  /**
   * Get an Option of result for a given type
   * @tparam T
   * @return Option of result for T
   */
  def get[T : ClassTag] : Option[T] = findTypeOnProductIterator(this.productIterator)

  /**
   * Get result for a given type, or a default value if type is not found
   * @param default
   * @tparam T
   * @return result for T or default
   */
  def getOrElse[T : ClassTag](default: T): T = get[T].getOrElse(default)

}

case class Union2[T1, T2](v1: Option[T1], v2: Option[T2]) extends Union

case class Union3[T1, T2, T3](v1: Option[T1], v2: Option[T2], v3: Option[T3]) extends Union

case class Union4[T1, T2, T3, T4](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4]) extends Union

case class Union5[T1, T2, T3, T4, T5](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4], v5: Option[T5]) extends Union

case class Union6[T1, T2, T3, T4, T5, T6](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4], v5: Option[T5], v6 : Option[T6]) extends Union

case class Union7[T1, T2, T3, T4, T5, T6, T7](v1: Option[T1], v2: Option[T2], v3: Option[T3], v4: Option[T4], v5: Option[T5], v6 : Option[T6], v7 : Option[T7]) extends Union

@UnionMacro
class Union2Type[T1, T2]

@UnionMacro
class Union3Type[T1, T2, T3]

@UnionMacro
class Union4Type[T1, T2, T3, T4]

@UnionMacro
class Union5Type[T1, T2, T3, T4, T5]

@UnionMacro
class Union6Type[T1, T2, T3, T4, T5, T6]

@UnionMacro
class Union7Type[T1, T2, T3, T4, T5, T6, T7]
