# Hamsters

A mini Scala utility library. Compatible with functional programming beginners. For the JVM and Scala.js.

<img src="https://img4.hostingpics.net/pics/987051hamsters.jpg" alt="hamster logo" width="150px"/>

Currently, Hamsters supports :

 * [Validation](docs/validation.md)
 * [Monad transformers](docs/monad_tranformers.md) 
 * [Enum typeclass](docs/enums.md)
 * [Default values for options (orEmpty)](docs/default_values.md)
 * [HLists](docs/hlist.md)
 * [Union types](docs/union_types.md)
 * [Retry util](docs/retry.md)
 * [NonEmptyList](docs/nonemptylist.md)
 * [Show](docs/show.md)


[![Travis](https://travis-ci.org/scala-hamsters/hamsters.svg?branch=master)](https://travis-ci.org/scala-hamsters/hamsters)

## Install as dependency

With SBT :

```scala
libraryDependencies ++= Seq(
  "io.github.scala-hamsters" %% "hamsters" % "2.1.0"
)
```

With Maven :

```xml
<dependency>
  <groupId>io.github.scala-hamsters</groupId>
  <artifactId>hamsters_${scala.version}</artifactId>
  <version>2.1.0</version>
</dependency>
```

For Scala.js :

```scala
libraryDependencies ++= Seq(
  "io.github.scala-hamsters" %%% "hamstersjs" % "2.1.0"
)
```

## 1.x to 2.0 Migration

`Validation.result` has been renamed to `Validation.run`.

## Extensions

See [hamsters-extensions](https://github.com/scala-hamsters/hamsters-extensions) for more information.

## Scaladoc

You can find the API documentation [here](http://scala-hamsters.github.io/hamsters/api).

## Special thanks

To [Laurencebeillaux](https://github.com/laurencebeillaux) who created the Hamsters logo.
