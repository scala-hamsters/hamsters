# Hamsters

A micro Scala utility library. Compatible with functional programming beginners :)

![hamster picture](http://loicdescotte.github.io/images/hamster.jpg)

## Simple validation

Statements can be `OK` or `KO`. Then you can get all successes and failures.

```scala
 val e1 = OK(1)
 val e2 = KO("error 1")
 val e3 = KO("error 2")
 
 val validation = Validation(e1,e2, e3)
 val failures = validation.failures //List[String] : List("error 1", "error 2")
 val successes = validation.successes //List[Int] : List(1)
```

Note : Validation works with standard Left and Right types.
 
##  Simple monad transformers

Example : combine Future and Option types then make it work in a for comprehension.  
More information on why it's useful [here](http://loicdescotte.github.io/posts/scala-compose-option-future/).

```scala
def foa: Future[Option[String]] = Future(Some("a"))
def fob(a: String): Future[Option[String]] = Future(Some(a+"b"))

val composedAB: Future[Option[String]] = (for {
  a <- FutureOption(foa)
  ab <- FutureOption(fob(a))
} yield ab).future
```
Currently hamsters only supports FutureEither and FutureOption monad transformers but more will come!

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

## OK biased Either

Either has no prefered side (`Left` or `Right`) in the standard Scala library. With this helper, `map` and `flatMap` can be used by default as on the right side of Either (i.e `OK`, or `Right` in the standard lib) for example in for comprehension. 


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
