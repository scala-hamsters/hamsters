# Retry

Retry allows to run a code block several times if it fails, with a customizable maximum number of tries. It accepts a function to log the error messages : 

```scala
val logErrorsFunction = (errorMessage: String) => println(errorMessage)

Retry(maxTries = 3, logErrorsFunction) {
  1+1
} // util.Success(2)
```