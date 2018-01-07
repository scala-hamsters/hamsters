package io.github.hamsters

import org.scalatest.{FlatSpec, Matchers}
import Memo._
import scala.collection.mutable.ArrayBuffer

class MemoSpec extends FlatSpec with Matchers {

  "Memo" should "memoize function results" in {

    val calculatedValues = new ArrayBuffer[String]
    val plusOne = memoize{(x: Int) => {
      val result = (x +1).toString
      calculatedValues += result
      result
    }}

    plusOne(1) should be("2")
    plusOne(2) should be("3")
    plusOne(1) should be("2")

    calculatedValues.size should be(2)
  }

  "Memo" should "memoize function results with multiple parameters" in {

    val calculatedValues = new ArrayBuffer[String]
    val plusOne: ((Int, Int)) => String = memoize{case (x: Int, y:Int) =>
      val result1 = x +1
      val result2 = y +1
      val result = s"$result1,$result2"
      calculatedValues += result
      result
    }

    plusOne(1,2) should be("2,3")
    plusOne(2,3) should be("3,4")
    plusOne(1,2) should be("2,3")

    calculatedValues.size should be(2)
  }
}
