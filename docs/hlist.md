
### HList

HLists can contain heterogeneous data types but are strongly typed. It's like tuples on steroids!
When you're manipulating data using tuples, it's common to add or subtrack some elements, but you have to make each element explicit to build a new tuple. HList simplifies this kind of task.

 * `::` is used to append elements at the beginning of an HList, and to construct new HLists
 * `+` is used add element at the end of a HList
 * `++` is used to concatenate 2 Hlists
 * HNil is the empty HList
 * other operations : filter, map, foldLeft, foreach

```scala
import io.github.hamsters.{HList, HCons, HNil}
import HList._

val hlist1: Double :: String :: HNil = 2.0 :: "hi" :: HNil
val hlist2 = 1 :: HNil

val sum = hlist1 + 1 // 2.0 :: "hi" :: 1 :: HNil
val sum2 = hlist1 ++ hlist2 // 2.0 :: "hi" :: 1 :: HNil

sum2.tail // hi :: (1 :: HNil)
sum2.head // 2.0 (Double)
sum2.tail.head // "hi" (String)

// Retrieve element by index and type
hlist1.get[String](1) // Some("hi")
// Or use apply to avoid Option
hlist1[String](1) // "hi"

(2.0 :: "hi" :: HNil).foldLeft("")(_+_) // "2.0hi"

(2.0 :: "hi" :: HNil).map(_.toString) // "2.0" :: "hi" :: HNil

(2.0 :: "hi" :: HNil).filter {
  case s: String if s.startsWith("h") => true
  case _ => false
} //"hi" :: HNil

```

### In next release (2.2) : HList <-> case class conversion macro

You can do HList to case class and case class to HList conversions :

```scala
@HListMacro
case class Person(name: String, age :Int, weight :Option[Int] = None)

Person(name = "Christophe Colomb", age = 42) //"Christophe Colomb"::42::None::HNil
HList.toClass[Person]("Christophe Colomb"::42::None::HNil) //Person(name = "Christophe Colomb", age = 42)
```
