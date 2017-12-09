package io.github.hamsters

import io.github.hamsters.MonadTransformers._
import io.github.hamsters.Validation._
import org.scalatest._

import scala.concurrent._
import MonadTransformers._

class MonadTransformersSpec extends AsyncFlatSpec with Matchers  {
  def foa: Future[Option[String]] = Future(Some("a"))
  def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))
  // FIXME test fail with scala.js (execution context issue) 
  "OptionT" should "handle Future[Option[_]] type" in {
   val composedAB: Future[Option[String]] = (for {
      a <- new OptionT(foa)
      ab <- new OptionT(fob(a))
    } yield ab)

    composedAB map { _ shouldBe Some("ab") }

    val noneString : Option[String] = None //TODO how to avoid type here?
    val composedABWithNone: Future[Option[String]] = (for {
      a <- new OptionT(Future.successful(noneString)) 
      ab <- new OptionT(fob(a))
    } yield ab) 

    composedABWithNone map { _ shouldBe None }

  }

  // FIXME test fail with scala.js (execution context issue) 
  "FutureOption" should "handle Future[Option[_]] type" in {
    def foa: Future[Option[String]] = Future(Some("a"))
    def fob(a: String): Future[Option[String]] = Future(Some(a + "b"))

    val composedAB= (for {
      a <- FutureOption(foa)
      ab <- FutureOption(fob(a))
    } yield ab)

    composedAB map { _ shouldBe Some("ab") }

    val noneString : Option[String] = None //TODO how to avoid type here?
    val composedABWithNone: Future[Option[String]] = (for {
      a <- FutureOption(Future.successful(noneString))
      ab <- FutureOption(fob(a))
    } yield ab)

    composedABWithNone map { _ shouldBe None }

    val failedFuture: Future[Option[String]] =  Future.failed(new Exception("d'oh!"))
    val composedABWithFailure: Future[Option[String]] = (for {
      a <- FutureOption(failedFuture)
      ab <- FutureOption(fob(a))
    } yield ab)

    composedABWithFailure.failed map { _ shouldBe a [Exception]}

  }

  "FutureOption" should "be filtered with pattern matching in for comprehension" in {

    def fo: Future[Option[(String, Int)]] = Future(Some(("a", 42)))

    val filtered = for {
      (a, i) <- FutureOption(fo) if i > 5
    } yield a

    filtered.wrapped map { _ shouldBe Some("a") }


    val filtered2 = for {
      (a, i) <- FutureOption(fo) if i > 50
    } yield a

    filtered2.wrapped  map { _ shouldBe None }


  }

  "FutureOption" should "handle future and option map/flatMap sequences" in {

    import io.github.hamsters.MonadTransformers._

    implicit def optiontoFutureOption[T](o: Option[T]): Future[Option[T]] = Future.successful(o)
    implicit def futuretoFutureOption[T](f: Future[T]): Future[Some[T]] = f.map(Some(_))

    case class User(id: Int)
    case class Data(id: Int)

    object Repo {
      def user(userId:Int) = Future(Some(User(userId)))
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
    operationSequence. map { _ shouldBe  "redirect" }

  }

  "FutureEither" should "handle Future[Either[_,_]] type" in {
    def fea: Future[Either[String, Int]] = Future(OK(1))
    def feb(a: Int): Future[Either[String, Int]] = Future(OK(a + 2))

    val composedAB: Future[Either[String, Int]] = for {
      a <- FutureEither(fea)
      ab <- FutureEither(feb(a))
    } yield ab

    composedAB map { _ shouldBe OK(3)}

    val composedABWithNone: Future[Either[String, Int]] = for {
      a <- FutureEither(Future.successful(KO("d'oh!")))
      ab <- FutureEither(feb(a))
    } yield ab

    composedABWithNone map { _ shouldBe  KO("d'oh!")}

    val composedABWithFailure: Future[Either[String, Int]] = for {
      a <- FutureEither(Future.failed(new Exception("d'oh!")))
      ab <- FutureEither(feb(a))
    } yield ab

    composedABWithFailure.failed map { _ shouldBe a [Exception]}

  }

  "FutureEither" should "be filtered with pattern matching in for comprehension" in {
    def fe: Future[Either[String, (String, Int)]] = Future(OK(("a", 42)))

    val filtered = for {
      (a, i) <- FutureEither(fe) if i > 5
    } yield a

    filtered.future map { _ shouldBe OK("a") }

    val filtered2 = for {
      (a, i) <- FutureEither(fe) if i > 50
    } yield a

    filtered2.future map { _ shouldBe KO("No value matching predicate") }

  }
}