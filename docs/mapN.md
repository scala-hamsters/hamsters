
# mapN


mapN provide the cartesian product of several encapsulated values.
The values can be encapsulated into List, Option, Future


Option  : 
```scala
import io.github.hamsters.Cartesian._
import io.github.hamsters.Functor._

val t1OptionInt: Option[Int] = Some(5)
val t2SomeInt: Option[Int] = Some(6)
val t3NoneInt: Option[Int] = None

(t1OptionInt, t2SomeInt).mapN(_ + _) // Some 11
(t1OptionInt, t3NoneInt).mapN(_ + _) // None

```

 
 Lists of  1 element :
 ```scala
(List(6), List(7)).mapN(_ * _)  // List(42)
 ```
 
 List of several elements
 
 ```scala
 (List(1, 2), List(3, 4)).mapN(_ + _) // List(4, 5, 5, 6)
 ```
 
 Future : 
 ```scala
 (Future.successful(66), Future.successful(77)).mapN(_ + _) // Future(143)
 ```
 
 Triplet : 
 ```scala
(List(6), List(7), List(8)).mapN( _ + _ + _ ) // List(21)
 ```

And so on until 22 parameters.
