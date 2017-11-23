# Default values for options (orEmpty)

Default values for options avoid repeating the same `getOrElse` code again and again : 

```scala
import io.github.hamsters.EmptyOptionValues._

val noneString: Option[String] = None
noneString.orEmpty  // ""

val noneInt: Option[Int] = None
noneInt.orEmpty // 0 

val noneList: Option[List[String]] = None
noneList.orEmpty  // List[String]()
```

To support other types, you just have to declare a new implicit value : 
 
 ```scala
implicit def myCaseClassDefaultValue= new DefaultValue[MyCaseClass] { ... }
```