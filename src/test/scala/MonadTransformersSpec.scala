import io.github.hamsters.{FutureEither, FutureOption}
import org.scalatest._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MonadTransformersSpec extends FlatSpec with Matchers {

  "FutureOption" should "handle Future[Option[_]] type" in {
    def foa: Future[Option[String]] = Future(Some("a"))
    def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

    val composedAB: Future[Option[String]] = (for {
      a <- FutureOption(foa)
      ab <- FutureOption(fob(a))
    } yield ab).future

    Await.result(composedAB, 1 second) shouldBe Some("ab")

    val composedABWithNone: Future[Option[String]] = (for {
      a <- FutureOption(Future.successful(None))
      ab <- FutureOption(fob(a))
    } yield ab).future

    Await.result(composedABWithNone, 1 second) shouldBe None

    val composedABWithFailure: Future[Option[String]] = (for {
      a <- FutureOption(Future.failed(new Exception("d'oh!")))
      ab <- FutureOption(fob(a))
    } yield ab).future

    an [Exception] should be thrownBy Await.result(composedABWithFailure, 1 second)
  }

  "FutureEither" should "handle Future[Either[_,_]] type" in {
    def foa: Future[Either[String, Int]] = Future(Right(1))
    def fob(a: Int): Future[Either[String, Int]] = Future(Right(a+2))

    val composedAB: Future[Either[String, Int]] = (for {
      a <- FutureEither(foa)
      ab <- FutureEither(fob(a))
    } yield ab).future

    Await.result(composedAB, 1 second) shouldBe Right(3)

    val composedABWithNone: Future[Either[String, Int]] = (for {
      a <- FutureEither(Future.successful(Left("d'oh!")))
      ab <- FutureEither(fob(a))
    } yield ab).future

    Await.result(composedABWithNone, 1 second) shouldBe Left("d'oh!")

    val composedABWithFailure: Future[Either[String, Int]] = (for {
      a <- FutureEither(Future.failed(new Exception("d'oh!")))
      ab <- FutureEither(fob(a))
    } yield ab).future

    an [Exception] should be thrownBy Await.result(composedABWithFailure, 1 second)
  }

}