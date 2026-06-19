# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Hamsters is a mini Scala utility library aimed at functional-programming beginners, cross-compiled for the JVM and Scala.js. It is built with sbt and targets **Scala 3.3.x LTS** (currently 3.3.8). The LTS line is chosen deliberately to keep TASTY/binary compatibility with Scala 2.13 consumers; there is no longer a Scala 2.x cross-build.

## Build & Test

This is an sbt project (Scala 3 only). Common commands:

```bash
sbt compile                      # compile both platforms
sbt test                         # run all tests (JVM + JS, via the root aggregate)
sbt clean test                   # the CI command (see .travis.yml)
```

The library is cross-built for JVM and Scala.js with explicit variants. To target one platform:

```bash
sbt hamstersJVM/test             # JVM only
sbt hamstersJS/test              # Scala.js only
```

Run a single test class or a single test, using ScalaTest selectors:

```bash
sbt "hamstersJVM/testOnly io.github.hamsters.ValidationSpec"
sbt "hamstersJVM/testOnly *ValidationSpec -- -z \"accumulate errors\""
```

## Architecture

The library is a single cross-project (`hamsters`), compiled for both JVM and JS via `sbt-crossproject` (`CrossType.Full`), wired in `build.sbt`. The project is **macro-free**: all code is plain Scala under `io.github.hamsters`.

- **`shared/`** — the cross-platform source. This is where features live (`Validation`, `Cartesian`/`mapN`, `Lens`, `Show`, `Enum`, `Monad`, `MonadTransformers`, `NonEmptyList`, `Memo`, `FutureOps`, `Monoid`/`Semigroup`/`Functor`, etc.).
- **`jvm/`** — JVM-only code that cannot be cross-compiled (e.g. a `Retry` variant using JVM threading).

`root` aggregates and depends on `hamstersJVM` and `hamstersJS` but does not itself publish (`noPublishSettings`).

Two features are arity-based and contain hand-written overloads for **arity 2–22**:
- `Validation.run` in `shared/.../Validation.scala` (accumulates all `Left`s into a `List[L]`, otherwise returns the tuple of `Right` values). It also defines `implicit def fromTry` so `Try` values can be passed where an `Either[String, _]` is expected.
- `Cartesian` in `shared/.../Cartesian.scala` defines `Tuple2Box`..`Tuple22Box` plus `tNx` implicit conversions that add `mapN` to tuples of `Box[T]` (requires `Functor[Box]` + `Cartesian[Box]` instances).

When changing the semantics of either, keep all 21 arities consistent — they were originally generated, so edit them as a block (a small script is the easiest way to regenerate them).

History note: this project previously used two macro systems (Scalameta annotation macros and scala-reflect def-macros). They were removed; `Lens`/`Showable`/`Enumerable` instances are now written by hand rather than derived.

## Source layout convention

All library code lives under the `io.github.hamsters` package. Sources follow the `sbt-crossproject` Full layout: `shared/src/{main,test}/scala/...` for cross-platform code, `jvm/src/...` for JVM-only code. Tests use ScalaTest, ScalaMock, and ScalaCheck (property-based tests, e.g. `*Laws`/`*Properties` specs).

## Documentation

User-facing feature docs are Markdown files in `docs/` (one per feature, indexed by `docs/README.md`). When adding or changing a feature, update its corresponding `docs/*.md` page. The release version is tracked in both `README.adoc` (`:release-version:`) and `build.sbt` (`version`).
