
# mapN


mapN provide the cartesian product of several encapsulated values.
The values can be encapsulated into List, Option, Future


Option  : 
```
val t1OptionInt: Option[Int] = Some(5)
val t2SomeInt: Option[Int] = Some(6)
val t3NoneInt: Option[Int] = None

(t1OptionInt, t2SomeInt).mapN(_ + _) // Some 11
(t1OptionInt, t3NoneInt).mapN(_ + _) // None

```

 
 List :
 ```
val t1List = List(6)
val t2List = List(7)

(t1List, t2List).mapN(_ * _)  // List(42)
 ```
 
 Future : 
 ```
 val t1FutureInt: Future[Int] = Future.successful(66)
 val t2FutureInt: Future[Int] = Future.successful(77)
 
 (t1FutureInt, t2FutureInt).mapN(_ + _) // Future(143)
 ```
 
 Triplet : 
 
 ```
val t1List = List(6)
val t2List = List(7)
val t3List = List(8)
(t1List, t2List, t3List).mapN( _ + _ + _ ) // List(21)
 ```