# Hamsters

A micro Scala utility library. Made for functional programming begginers :)

## Basic validation

```scala
 val e1: Either[String, Int] = Right(1)
 val e2: Either[String, Int] = Left("nan")
 val e3: Either[String, Int] = Left("nan2")
 
 val validation = Validation(e1,e2, e3)
 val failures = validation.failures //List[String] : List("nan", "nan2")
 val successes = validation.successes //List[Int] : List(1)
```
 
##  Basic monad transformers

```scala
def foa: Future[Option[String]] = Future(Some("a"))
def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

val composedAB: Future[Option[String]] = (for {
  a <- FutureOption(foa)
  ab <- FutureOption(fob(a))
} yield ab).future

```
Currently hamsters only supports FutureEither and FutureOption monad transformers but more will come!

## Right biased Either

`map` and `flatMap` can be used by default as on the right side of Either, for example in for comprehension. 

```scala
import io.github.hamsters.Implicits._

val e1: Either[String, Int] = Right(1)
val e2: Either[String, Int] = Left("nan")
val e3: Either[String, Int] = Left("nan2")

// Stop at first error
for {
  v1 <- e1
  v2 <- e2
  v3 <- e3
} yield(s"$v1-$v2-$v3")  //Left("nan")
```
 
## Coming soon 
 * basic HList with conversions from/to tuples
 * basic type Union
