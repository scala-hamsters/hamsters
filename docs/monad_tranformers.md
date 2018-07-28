#  Monad transformers

Example : combine Future and Option types then make it work in a for comprehension.
More information on why it's useful [here](http://loicdescotte.github.io/posts/scala-compose-option-future/).

## FutureOption

```scala
import io.github.hamsters.FutureOption
import io.github.hamsters.MonadTransformers._
//import your execution context here too, for example import scala.concurrent.ExecutionContext.Implicits.global

def foa: Future[Option[String]] = Future(Some("a"))
def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

val composedAB: Future[Option[String]] = for {
  a <- FutureOption(foa)
  ab <- FutureOption(fob(a))
} yield ab
```

## FutureTry

```scala
import io.github.hamsters.FutureTry
import io.github.hamsters.MonadTransformers._
//import your execution context here too, for example import scala.concurrent.ExecutionContext.Implicits.global

def foa: Future[Try[String]] = Future(Try("a"))
def fob(a: String): Future[Try[String]] = Future(Try(a+"b"))

val composedAB: Future[Try[String]] = for {
  a <- FutureTry(foa)
  ab <- FutureTry(fob(a))
} yield ab
```

## FutureEither

```scala
import io.github.hamsters.FutureEither
import io.github.hamsters.MonadTransformers._
//import your execution context here too, for example import scala.concurrent.ExecutionContext.Implicits.global

def fea: Future[Either[String, Int]] = Future(Right(1))
def feb(a: Int): Future[Either[String, Int]] = Future(Right(a+2))

val composedAB: Future[Either[String, Int]] = for {
  a <- FutureEither(fea)
  ab <- FutureEither(feb(a))
} yield ab
```

## OptionT and EitherT

If you use other combinations of F[Option[A]] or F[Either[L,R]], you can bring your own `Monad` implicit instance and work with `OptionT` or `EitherT`. `Future` and `Option` instances are provided by default.

---

Note : For Future[Try] composition, see `Future.fromTry` to get a single future success or failure.
