package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}

class LenserSpec extends FlatSpec with Matchers {

  @GenLens
  case class ObjectLevel2(objectLevel3 : String)

  @GenLens
  case class ObjectLevel1(objectLevel2 : ObjectLevel2)

  @GenLens
  case class RootObject(objectLevel1 : ObjectLevel1)

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
