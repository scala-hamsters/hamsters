# Hamsters

A micro Scala utility library. Compatible with functional programming beginners :)

Currently, Hamsters supports :

 * Validation
 * OK/KO Monads
 * Monad transformers
 * HLists
 * Union types

![hamster picture](http://loicdescotte.github.io/images/hamster.jpg)

[![Travis](https://travis-ci.org/scala-hamsters/hamsters.svg?branch=master)](https://travis-ci.org/scala-hamsters/hamsters)

## Install as dependency

```scala
libraryDependencies ++= Seq(
  "io.github.scala-hamsters" %% "hamsters" % "1.0-BETA1"
)

resolvers += Resolver.url("github repo for hamsters", url("http://scala-hamsters.github.io/hamsters/releases/"))(Resolver.ivyStylePatterns)
```

## Validation and monadic OK/KO

Statements can be `OK` or `KO`. Then you can get all successes and failures.

```scala
 val e1 = OK(1)
 val e2 = KO("error 1")
 val e3 = KO("error 2")
 
 val validation = Validation(e1,e2, e3)
 val failures = validation.failures //List[String] : List("error 1", "error 2")
 val successes = validation.successes //List[Int] : List(1)
```

You can also use OK/KO in a monadic way if you want to stop processing at the first encountered error.

```scala
import io.github.hamsters.Implicits._

val e1: Either[String, Int] = OK(1)
val e2: Either[String, Int] = KO("nan")
val e3: Either[String, Int] = KO("nan2")

// Stop at first error
for {
  v1 <- e1
  v2 <- e2
  v3 <- e3
} yield(s"$v1-$v2-$v3")  //KO("nan")
```

Note : Validation relies on standard Either, Left and Right types.
 
##  Monad transformers

Example : combine Future and Option types then make it work in a for comprehension.  
More information on why it's useful [here](http://loicdescotte.github.io/posts/scala-compose-option-future/).

### FutureEither

```scala
def fea: Future[Either[String, Int]] = Future(OK(1))
def feb(a: Int): Future[Either[String, Int]] = Future(OK(a+2))

val composedAB: Future[Either[String, Int]] = (for {
  a <- FutureEither(fea)
  ab <- FutureEither(feb(a))
} yield ab).future
```
### FutureOption

```scala
def foa: Future[Option[String]] = Future(Some("a"))
def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

val composedAB: Future[Option[String]] = (for {
  a <- FutureOption(foa)
  ab <- FutureOption(fob(a))
} yield ab).future
```

## HList

HLists can contain heterogeneous data types but are strongly typed. It's like tuples on steroids!
`::` is used to append elements at the beggining of an HList. `++` is used to concatenate 2 Hlists.
 
```scala
val hlist = 2.0 :: "hi" :: HNil

hlist ++ ("hello" :: true :: HNil) //(2.0 :: (hi :: (hello :: (true :: HNil))))

(2.0 :: "hi" :: HNil).foldLeft("")(_+_) // "2.0hi"

(2.0 :: "hi" :: HNil).map(_.toString) // "2.0" :: "hi" :: HNil

(2.0 :: "hi" :: HNil).filter{
      case s: String if s.startsWith("h") => true
      case _ => false
    } //"hi" :: HNil

```

## Union types

You can define functions or methods that are able to return several types, depending on the context.

```scala
//json element can contain a String, a Int or a Double
def jsonElement(x: Int): Union3[String, Int, Double] = {
  if(x == 0) "0"
  else if (x % 2 == 0) 1
  else 2.0
}
```
