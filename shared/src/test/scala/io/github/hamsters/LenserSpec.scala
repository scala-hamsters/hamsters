package io.github.hamsters

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LenserSpec extends AnyFlatSpec with Matchers {

  case class ObjectLevel2(objectLevel3 : String)
  object ObjectLevel2 {
    val _objectLevel3 = new Lens[ObjectLevel2, String] {
      override def get: ObjectLevel2 => String = _.objectLevel3
      override def set: ObjectLevel2 => String => ObjectLevel2 = s => v => s.copy(objectLevel3 = v)
    }
  }

  case class ObjectLevel1(objectLevel2 : ObjectLevel2)
  object ObjectLevel1 {
    val _objectLevel2 = new Lens[ObjectLevel1, ObjectLevel2] {
      override def get: ObjectLevel1 => ObjectLevel2 = _.objectLevel2
      override def set: ObjectLevel1 => ObjectLevel2 => ObjectLevel1 = s => v => s.copy(objectLevel2 = v)
    }
  }

  case class RootObject(objectLevel1 : ObjectLevel1)
  object RootObject {
    val _objectLevel1 = new Lens[RootObject, ObjectLevel1] {
      override def get: RootObject => ObjectLevel1 = _.objectLevel1
      override def set: RootObject => ObjectLevel1 => RootObject = s => v => s.copy(objectLevel1 = v)
    }
  }

  val instance = RootObject(ObjectLevel1(ObjectLevel2("initial value")))

  "set a value with composed lens"  should "set the value" in {
    import RootObject._
    import ObjectLevel1._
    import ObjectLevel2._
    (_objectLevel1 composeLens _objectLevel2 composeLens _objectLevel3).set(instance)("new value") == RootObject(ObjectLevel1(ObjectLevel2("new value")))
  }

  "set a value with composed (using symbol) lens"  should "set the value" in {
    import RootObject._
    import ObjectLevel1._
    import ObjectLevel2._
    (_objectLevel1 >>> _objectLevel2 >>> _objectLevel3).set(instance)("new value") == RootObject(ObjectLevel1(ObjectLevel2("new value")))
  }

}
