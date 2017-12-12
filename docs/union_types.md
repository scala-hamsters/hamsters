# Union types

You can define functions or methods that are able to return several types, depending on the context.

```scala
import io.github.hamsters.{Union3, Union3Type}

//json element can contain a String, a Int or a Double
val jsonUnion = new Union3Type[String, Int, Double]
import jsonUnion._

def jsonElement(x: Int): Union3[String, Int, Double] = {
  if(x == 0) "0"
  else if (x % 2 == 0) 1
  else 2.0
}
```

Then you can use pattern matching, or ask for a specifc type and retrieve an option :

```scala
jsonElement(0).get[String] // Some("0")
jsonElement(1).getOrElse("not found") // get String value or "not found" if get[String] is undefined
```