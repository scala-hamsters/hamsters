###  Monad transformers

Example : combine Future and Option types then make it work in a for comprehension.
More information on why it's useful [here](http://loicdescotte.github.io/posts/scala-compose-option-future/).

#### FutureOption

```scala
def foa: Future[Option[String]] = Future(Some("a"))
def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

val composedAB: Future[Option[String]] = for {
  a <- FutureOption(foa)
  ab <- FutureOption(fob(a))
} yield ab
```

#### FutureEither

```scala
import io.github.hamsters.Validation._
import io.github.hamsters.{FutureEither, FutureOption}
import io.github.hamsters.MonadTransformers._

def fea: Future[Either[String, Int]] = Future(Right(1))
def feb(a: Int): Future[Either[String, Int]] = Future(Right(a+2))

val composedAB: Future[Either[String, Int]] = for {
  a <- FutureEither(fea)
  ab <- FutureEither(feb(a))
} yield ab
```

Note 1 : You can also bring your own `Monad` implicit instance and work with `OptionT` or `EitherT`. 
`Future` and `Option` instances are provided by default.

Note 2 : For Future[Try] composition, see `Future.fromTry` to get a single future success or failure.
