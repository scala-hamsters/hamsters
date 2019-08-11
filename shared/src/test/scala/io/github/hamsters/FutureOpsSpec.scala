package io.github.hamsters

import org.scalatest._
import scala.concurrent.{ExecutionContext, _}
import scala.util._

class FutureOpsSpec extends AsyncFlatSpec with Matchers {

  override implicit val executionContext = ExecutionContext.global

  import FutureOps._

  def fea: Future[Either[Throwable, Int]] = Future(Right(1))

  def feb(a: Int): Future[Either[Throwable, String]] = Future(Right(s"${a}b"))

  abstract class Error(message: String) extends Exception(message)

  case object BoomError extends Error("Boom")

  "FutureOps" should "convert Future[Right[Throwable, A]] to Future[A]" in {
    FutureOps.fromEither(Right("a")) map {
      _ shouldBe "a"
    }   
  }

  "FutureOps" should "convert Future[Left[Throwable, A]] to Future[A]" in {   
    FutureOps.fromEither(Left(BoomError)).failed map {
      _ shouldBe BoomError
    }
  }

  "FutureOps" should "squash Future[Right[Throwable, A]]" in {
    val composedAB: Future[String] = for {
      a <- fea.squash
      ab <- feb(a).squash
    } yield ab

    composedAB map {
      _ shouldBe "1b"
    }

  }

   "FutureOps" should "squash Future[Left[Throwable, A]]" in {
    val error: Either[Throwable, Int] = Left(BoomError)
    val composedABWithError: Future[String] = for {
      a <- Future.successful(error).squash
      ab <- feb(a).squash
    } yield ab

    composedABWithError.failed map {
      _ shouldBe BoomError
    }

  }

  def foa: Future[Option[String]] = Future(Some("a"))

  def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

  "FutureOps" should "convert Future[Nome[A]] to Future[A]" in {
    FutureOps.fromOption(Some("a")) map (_ shouldBe "a")
  }

  "FutureOps" should "convert Future[None] to Future[A]" in {
    FutureOps.fromOption(None).failed map {
      _ shouldBe a[EmptyValueError]
    }
  }

  "FutureOps" should "squash Future[Some[A]]" in {
    val composedAB: Future[String] = for {
      a <- foa.squash
      ab <- fob(a).squash
    } yield ab

    composedAB map {
      _ shouldBe "ab"
    }

  }

  "FutureOps" should "squash Future[None]" in {   
    val noneString: Option[String] = None
    val composedABWithNone: Future[String] = for {
      a <- Future.successful(noneString).squash
      ab <- fob(a).squash
    } yield ab

    composedABWithNone.failed map {
      _ shouldBe a[EmptyValueError]
    }

  }

  def fta: Future[Try[String]] = Future(Success("a"))

  def ftb(a: String): Future[Try[String]] = Future(Success(a + "b"))

  "FutureOps" should "squash Future[Success[A]]" in {
    val composedAB: Future[String] = for {
      a <- fta.squash
      ab <- ftb(a).squash
    } yield ab

    composedAB map {
      _ shouldBe "ab"
    }
  }

  "FutureOps" should "squash Future[Failure[E]" in {
    val exception = new Exception()
    val failedString: Try[String] = Failure(exception)
    val composedABWithFailure: Future[String] = for {
      a <- Future.successful(failedString).squash
      ab <- ftb(a).squash
    } yield ab

    composedABWithFailure.failed map {
      _ shouldBe a[Exception]
    }
  }

}
