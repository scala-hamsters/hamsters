import sbt.Keys._

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "io.github.scala-hamsters",
  version := "dev",
  scalacOptions ++= Seq(),
  scalaVersion := "2.12.8",  
  scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),  
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
    "org.scalameta" %% "scalameta" % "1.8.0"
  ),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in(Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  sources in(Compile, doc) := Nil, // macroparadise doesn't work with scaladoc yet.
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")
)

lazy val jvm = project.settings(
  name := "jvm",
  buildSettings
)

lazy val metas = project.settings(
  name := "metas",
  buildSettings
)

lazy val macros = project.settings(
  name := "macros",
  buildSettings ++ Seq(
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value),
    resolvers += Resolver.sonatypeRepo("releases")
)

lazy val shared = project.settings(
  name := "shared",
  buildSettings
).dependsOn(macros, metas)

lazy val global = project
.in(file("."))
.settings(buildSettings ++ Seq(
    name := "hamsters"
  )
)
.dependsOn(metas, macros, shared, jvm)
.aggregate(metas, macros, shared, jvm)

