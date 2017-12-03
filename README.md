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
  "io.github.scala-hamsters" %% "hamsters" % "2.1.2"
)
```

With Maven :

```xml
<dependency>
  <groupId>io.github.scala-hamsters</groupId>
  <artifactId>hamsters_${scala.version}</artifactId>
  <version>2.1.2</version>
</dependency>
```

For Scala.js :

```scala
libraryDependencies ++= Seq(
  "io.github.scala-hamsters" %%% "hamsters" % "2.1.2"
)
```

## Try Hamster with an Ammonite instant REPL

You can try quickly and easily Hamsters with script try-hamster.sh, this script downloads and installs 
[Coursier](https://github.com/alexarchambault/coursier), [Ammonite](https://github.com/lihaoyi/Ammonite) REPL and Hamsters dependency. 
No dependencies needed other than a JDK.

PS: The macros don't work with the REPL

```shell
curl -s https://raw.githubusercontent.com/scala-hamsters/hamsters/repl/scripts/try-hamsters.sh | bash

Loading...
Welcome to the Ammonite Repl 1.0.1
(Scala 2.12.3 Java 1.8.0_151)


@ val noneString: Option[String] = None 
noneString: Option[String] = None

@ val hlist1: Double :: String :: HNil = 2.0 :: "hi" :: HNil 
hlist1: Double :: String :: HNil = HCons(2.0, HCons("hi", HNIL))

@ exit 
Bye!

```

## 1.x to 2.0 Migration

`Validation.result` has been renamed to `Validation.run`.

## Extensions

See [hamsters-extensions](https://github.com/scala-hamsters/hamsters-extensions) for more information.

## Scaladoc

You can find the API documentation [here](http://scala-hamsters.github.io/hamsters/doc/2.1/api).

## Special thanks

To [Laurencebeillaux](https://github.com/laurencebeillaux) who created the Hamsters logo.
