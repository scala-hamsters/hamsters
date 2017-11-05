# Retry

Retry allows to run a code block several times if it fails, with a customizable maximum number of tries. It accepts a function to log the error messages : 

```scala
import io.github.hamsters.Retry

val logErrorsFunction = (errorMessage: String) => println(errorMessage)

Retry(maxTries = 3, logErrorsFunction) {
  1+1
} // util.Success(2)
```

Note : see the `fromTry` method if your code already return a `Try`.

You can ask for a time to wait before a retry : 

```scala
import io.github.hamsters.jvm.Retry

 val result = Retry.withWait(3, 3000, logErrorsFunctionMock) {
      1 + 1
} // Future(Success(2))
```

To avoid blocking in the main Thread, this method returns a `Future[T]`.

Note : `withWait` is only available on the JVM as blocking is not compatible with ScalaJS.
