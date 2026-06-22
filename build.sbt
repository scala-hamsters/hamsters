import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

// Publishing is handled by sbt-ci-release (Central Portal).
// The version is derived from the git tag by sbt-dynver:
//   - on a tag `X.Y.Z`           -> version `X.Y.Z`
//   - otherwise                  -> `<lastTag>+<n>-<sha>-SNAPSHOT`
// `dynverVTagPrefix := false` lets dynver read bare tags such as `4.0.0`
// (no `v` prefix), matching the existing tagging scheme of this project.
inThisBuild(List(
  organization := "io.github.scala-hamsters",
  description := "A mini Scala utility library for functional-programming beginners, for the JVM and Scala.js.",
  homepage := Some(url("https://github.com/scala-hamsters/hamsters")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "hamstersTeam",
      "Hamsters Team",
      "",
      url("https://github.com/scala-hamsters/hamsters/graphs/contributors")
    )
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/scala-hamsters/hamsters"),
      "scm:git@github.com:scala-hamsters/hamsters.git"
    )
  ),
  versionScheme := Some("early-semver"),
  dynverVTagPrefix := false,
  scalaVersion := "3.3.8"
))

val commonSettings = Seq(
  scalacOptions ++= Seq("-language:implicitConversions", "-feature", "-deprecation")
)

val noPublishSettings = Seq(
  publish / skip := true
)

lazy val hamsters = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.2.19" % Test,
    "org.scalamock" %%% "scalamock" % "6.0.0" % Test,
    "org.scalacheck" %%% "scalacheck" % "1.18.1" % Test
  ))

lazy val hamstersJVM = hamsters.jvm.settings(name := "hamsters")
lazy val hamstersJS = hamsters.js.settings(name := "hamsters")

lazy val root = project.in(file("."))
  .aggregate(hamstersJVM, hamstersJS)
  .dependsOn(hamstersJVM, hamstersJS)
  .settings(commonSettings)
  .settings(noPublishSettings)
