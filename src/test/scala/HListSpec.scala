import org.scalatest.{FlatSpec, Matchers}
import io.github.hamsters.HNil

class HListSpec extends FlatSpec with Matchers{

  "HList" should "cons from elements" in {

    ("hi" :: HNil).tail shouldBe HNil
    ("hi" :: HNil).head shouldBe "hi"

    (1 :: "hi" :: HNil).tail shouldBe "hi" :: HNil
    (1 :: "hi" :: HNil).head shouldBe 1

  }

  "HList" should "be appenable" in {

    ((2.0 :: "hi" :: HNil) ++ (1 :: HNil)) shouldBe 2.0 :: "hi" :: 1 :: HNil
    ((2.0 :: "hi" :: HNil) ++ (1 :: HNil)).head shouldBe 2.0

  }

  "HList" should "be foldable" in {

    (2.0 :: "hi" :: HNil).foldLeft("")(_+_) shouldBe "2.0hi"

  }


  "HList" should "be mappable" in {

    (2.0 :: "hi" :: HNil).map(_.toString) shouldBe ("2.0" :: "hi" :: HNil)

  }

}
