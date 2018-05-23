# Show

Clazz provides string representation of class type.

```scala
import io.github.hamsters.Clazz

class Foo
case class Bar(x : String)
case object Quix


Clazz.getSimpleName[Foo] // Foo
Clazz.getSimpleName[Bar] //Bar
Clazz.getSimpleName[Quix.type ] //Quix
```

