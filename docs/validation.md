# Validation

Statements can be successful or fail. Validation helps you to retrieve values or failures for a sequence of statements.

```
  Note : Validation relies on standard Either types.
  Left is used for failures, Right for correct results.
  You can also use Invalid as an alias for Left and Valid as an alias for Right.
```

```scala
import io.github.hamsters.Validation
import Validation._

val e1 = Valid(1)
val e2 = Invalid("error 1")
val e3 = Invalid("error 2")
val e4 = Valid("4")

Validation.run(e1, e2, e3) // Invalid(List[String]) : Invalid(List("error 1", "error 2"))
Validation.run(e1, e4) // Valid((1, "4"))

Validation.failures(e1, e2, e3) // List[String] : List("error 1", "error 2")
Validation.failures(e1, e4) // Nil
```

To automatically catch exceptions into a Left or KO object, you can use `fromCatchable`.
By default it will give you an error message, but you can specify what to do in error cases :

```scala
def compute(x: Int):Int = 2/x

fromCatchable(compute(1)) //Valid(2)
fromCatchable(compute(0)) //Invalid("/ by zero")

fromCatchable(compute(0), (t: Throwable) => t.getClass.getSimpleName) //Invalid("ArithmeticException")
```

```
Note : You can also use Valid/Invalid (Right/Left) in a monadic way (for example in a for-comprehension) if you want to stop processing at the first encountered error, even with Scala 2.11.
To make it work with Scala 2.11, just import Validation.RightBiasedEither.
```

## Remarks: Try

You can use `Try` types with `Validation`:

```scala
Validation.run(
  Failure(new Exception("nan")),
  Success(1),
  Success("2"),
  Failure(new Exception("error 2")),
  Success("3")
) // Invalid(List("nan", "error 2"))
```
