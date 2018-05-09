# Sealed

Sealed allows to list the child of a sealed trait

```
sealed trait Colors
case object Red extends Colors
case object Orange extends Colors
case object Green extends Colors

Sealed.values[Colors] //Set(Red, Orange, Green)
```
