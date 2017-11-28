# Show

Show provides a textual representation. You have to put an annotation on your case class.

```scala
@ShowMacro
case class Name(firstName: String, lastName: String)

val n = Name("john", "doe")
Show.show(n) //"Name(firstName=john,lastName=doe)
```


It's also can be a deepest case class like (but all the cases classes must be annotated) : 

```scala 
@ShowMacro
case class Name(firstName: String, lastName: String)

@ShowMacro
case class Person(name : Name, age: Int)

val p = Person(Name("john", "doe"), 35)
Show.show(p) // "Person(name=Name(firstName=john,lastName=doe),age=35)"

```

## Depedencies 

To use Show macro, you need to add this dependencies to your build : 

```scala
libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0" % Provided
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
```