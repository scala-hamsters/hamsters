package io.github.hamsters

import io.github.hamsters.MonadTransformers._
import io.github.hamsters.Validation._
import org.scalatest._
import scala.concurrent._
import scala.util.{Try, Success, Failure}
import MonadTransformers._
import scala.concurrent.ExecutionContext

class MonadTransformersSpec extends AsyncFlatSpec with Matchers {

  override implicit val executionContext = ExecutionContext.global

  def foa: Future[Option[String]] = Future(Some("a"))

  def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

  "OptionT" should "handle Future[Some[_]] type" in {
    val composedAB: Future[Option[String]] = for {
      a <- new OptionT(foa)
      ab <- new OptionT(fob(a))
    } yield ab

    composedAB map {
      _ shouldBe Some("ab")
    }
  }

  "OptionT" should "handle Future[None] type" in {
    val noneString: Option[String] = None
    val composedABWithNone: Future[Option[String]] = for {
      a <- new OptionT(Future.successful(noneString))
      ab <- new OptionT(fob(a))
    } yield ab

    composedABWithNone map {
      _ shouldBe None
    }

  }

  "FutureOption" should "handle Future[Some[_]] type" in {
    def foa: Future[Option[String]] = Future(Some("a"))

    def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

    val composedAB: Future[Option[String]] = for {
      a <- FutureOption(foa)
      ab <- FutureOption(fob(a))
    } yield ab

    composedAB map {
      _ shouldBe Some("ab")
    }
  }

  "FutureOption" should "handle Future[None] type" in {
    def foa: Future[Option[String]] = Future(Some("a"))

    def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

    val noneString: Option[String] = None
    val composedABWithNone: Future[Option[String]] = for {
      a <- FutureOption(Future.successful(noneString))
      ab <- FutureOption(fob(a))
    } yield ab

    composedABWithNone map {
      _ shouldBe None
    }
  }

  "FutureOption" should "handle failed Future" in {
    def foa: Future[Option[String]] = Future(Some("a"))

    def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

    val failedFuture: Future[Option[String]] = Future.failed(new Exception("d'oh!"))
    val composedABWithFailure: Future[Option[String]] = for {
      a <- FutureOption(failedFuture)
      ab <- FutureOption(fob(a))
    } yield ab

    composedABWithFailure.failed map {
      _ shouldBe a[Exception]
    }

  }

  "Future Some" should "be filtered with pattern matching in for comprehension" in {

    def fo: Future[Option[(String, Int)]] = Future(Some(("a", 42)))

    val filtered = for {
      (a, i) <- FutureOption(fo) if i > 5
    } yield a

    filtered.wrapped map {
      _ shouldBe Some("a")
    }

  }

  "Future None" should "be filtered with pattern matching in for comprehension" in {

    def fo: Future[Option[(String, Int)]] = Future(Some(("a", 42)))

    val filtered2 = for {
      (a, i) <- FutureOption(fo) if i > 50
    } yield a

    filtered2.wrapped map {
      _ shouldBe None
    }

  }

  "FutureOption" should "handle future and option map/flatMap sequences" in {

    implicit def optiontoFutureOption[T](o: Option[T]): Future[Option[T]] = Future.successful(o)

    implicit def futuretoFutureOption[T](f: Future[T]): Future[Some[T]] = f.map(Some(_))

    case class User(id: Int)
    case class Data(id: Int)

    object Repo {
      def user(userId: Int) = Future(Some(User(userId)))

      def getData(id: Int) = Future(Data(1))

      def updateData(id: Int) = Future(2)
    }

    type Result = String
    val userIdOption = Some(1)

    val operationSequenceOpt =
      (for {
        userId <- FutureOption(userIdOption)
        user <- FutureOption(Repo.user(userId))
        data <- FutureOption(Repo.getData(user.id))
        _ <- FutureOption(Repo.updateData(data.id))
      } yield {
        "redirect"
      }).wrapped

    val operationSequence = operationSequenceOpt.map(_.getOrElse("error"))
    operationSequence.map {
      _ shouldBe "redirect"
    }

  }

  def fea: Future[Either[String, Int]] = Future(Valid(1))

  def feb(a: Int): Future[Either[String, Int]] = Future(Valid(a + 2))

  "FutureEither" should "handle Future[Right[_,_]] type" in {
    val composedAB: Future[Either[String, Int]] = for {
      a <- FutureEither(fea)
      ab <- FutureEither(feb(a))
    } yield ab

    composedAB map {
      _ shouldBe Valid(3)
    }
  }

  "FutureEither" should "handle Future[Left[_,_]] type" in {
    val koString: Left[String, Int] = Invalid("d'oh!")

    val composedABWithNone: Future[Either[String, Int]] = for {
      a <- FutureEither(Future.successful(koString))
      ab <- FutureEither(feb(a))
    } yield ab

    composedABWithNone map {
      _ shouldBe Invalid("d'oh!")
    }

  }

  "FutureEither" should "handle failed Future" in {
    val failedFuture: Future[Either[String, Int]] = Future.failed(new Exception("d'oh!"))
    val composedABWithFailure: Future[Either[String, Int]] = for {
      a <- FutureEither(failedFuture)
      ab <- FutureEither(feb(a))
    } yield ab

    composedABWithFailure.failed map {
      _ shouldBe a[Exception]
    }

  }

  "Future Right" should "be filtered with pattern matching in for comprehension" in {
    def fe: Future[Either[String, (String, Int)]] = Future(Valid(("a", 42)))

    val filtered = for {
      (a, i) <- FutureEither(fe) if i > 5
    } yield a

    filtered.wrapped map {
      _ shouldBe Valid("a")
    }

  }

  "Future Left" should "be filtered with pattern matching in for comprehension" in {
    def fe: Future[Either[String, (String, Int)]] = Future(Valid(("a", 42)))

    val filtered = for {
      (a, i) <- FutureEither(fe) if i > 50
    } yield a

    filtered.wrapped map {
      _ shouldBe Invalid("No value matching predicate")
    }

  }

  def fta: Future[Try[Int]] = Future(Try(1))

  def ftb(a: Int): Future[Try[Int]] = Future(Try(a + 2))

  "FutureTry" should "handle Future[Success[_]] type" in {
    val composedAB: Future[Try[Int]] = for {
      a <- FutureTry(fta)
      ab <- FutureTry(ftb(a))
    } yield ab

    composedAB map {
      _ shouldBe Success(3)
    }
  }

  "FutureTry" should "handle Future[Failure[_]] type" in {
    val exc = new Exception("d'oh!")
    val koString: Try[Int] = Failure(exc)

    val composedABWithFailure: Future[Try[Int]] = for {
      a <- FutureTry(Future.successful(koString))
      ab <- FutureTry(ftb(a))
    } yield ab

    composedABWithFailure map {
      _ shouldBe Failure(exc)
    }
  }

  "FutureTry" should "handle failed Future" in {
    val failedFuture: Future[Try[Int]] = Future.failed(new Exception("d'oh!"))
    val composedABWithFailure: Future[Try[Int]] = for {
      a <- FutureTry(failedFuture)
      ab <- FutureTry(ftb(a))
    } yield ab

    composedABWithFailure.failed map {
      _ shouldBe a[Exception]
    }
  }

  "Future Success" should "be filtered with pattern matching in for comprehension" in {
    def ft: Future[Try[(String, Int)]] = Future(Try(("a", 42)))

    val filtered = for {
      (a, i) <- FutureTry(ft) if i > 5
    } yield a

    filtered.wrapped map {
      _ shouldBe Success("a")
    }
  }
}
