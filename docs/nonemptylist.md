# NonEmptyList

A data type which represents a non-empty list with a single element (the head)
and an optional tail.

```scala
NonEmptyList(42).head // 42
NonEmptyList(0, 1, 2).tail // List(1, 2)
```
