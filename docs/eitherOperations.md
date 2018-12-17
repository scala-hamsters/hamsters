# Either operations

Statements can be successful or fail. Either operations help you kown if there are failures and to retrieve all all of them (no quick fail) for a sequence of statements.


```scala
import io.github.hamsters.EitherOps._

sealed trait Error
case class NumericError(message: String) extends Error
case class OtherError(message: String) extends Error

val e1 = Right(1)
val e2 = Left(NumericError("nan"))
val e3 = Left(OtherError("foo"))

val errors: Seq[Error] Seq(e1,e2,e3).collectLefts
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