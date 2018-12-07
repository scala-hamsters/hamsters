# Validation

Statements can be successful or fail. Validation helps you kown if there are failures and to retrieve all all of them (no quick fail) for a sequence of statements.

```
  Note : Validation relies on standard Either types.
  Left is used for failures, Right for correct results.
```

```scala
import io.github.hamsters.Validation
import Validation._

val e1 = Right(1)
val e2 = Left("error 1")
val e3 = Left("error 2")
val e4 = Right("4")

Validation.hasFailures(e1, e2, e3) //true
Validation.failures(e1, e2, e3) //List[String] : List("error 1", "error 2")
Validation.hasFailures(e1, e4) //false
Validation.failures(e1, e4) // Nil
```

If there are no failures you can use a for comprehension to get all succesful values.

To automatically catch exceptions into a Left object, you can use `fromCatchable`.
By default it will give you an error message, but you can specify what to do in error cases :

```scala
def compute(x: Int):Int = 2/x

fromCatchable(compute(1)) //Right(2)
fromCatchable(compute(0)) //Left("/ by zero")

fromCatchable(compute(0), (t: Throwable) => t.getClass.getSimpleName) //Left("ArithmeticException")
```