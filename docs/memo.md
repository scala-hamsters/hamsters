# Memoization

Memo helps to memoize results for idempotent functions.

## Single parameter function 

This function takes a Int and returns a String : 

```scala
import io.github.hamsters.Memo._

val plusOne = memoize{ (x: Int) => 
  (x +1).toString
}

plusOne(1) //"2"
plusOne(2) //"3"
plusOne(1) //"2" <-- the function is not really called this time as the result has been memoized before
```

## Multiple parameter function

This function takes two Int and returns a String : 

```scala
import io.github.hamsters.Memo._

val plusOne: ((Int, Int)) => String = memoize{ case (x: Int, y:Int) =>
  val result1 = x +1
  val result2 = y +1
  s"$result1,$result2"
}

plusOne(1,2) //"2,3"
plusOne(2,3) //"3,4"
plusOne(1,2) //"2,3" <-- the function is not really called this time as the result has been memoized before
```

## Thread safety

For a thread safe implementation, use Memo.threadSafeMemoize instead of Memo.memoize.
