import sbt.Keys._

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "io.github.scala-hamsters",
  version := "1.0.8-SNAPSHOT",
  scalacOptions ++= Seq(),
  scalaVersion := "2.11.8",
  scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val macros = Project(
  "macros",
  file("macros"),
  settings = buildSettings
)

lazy val root = Project(
  "root",
  file("."),
  settings = buildSettings ++ Seq(
    name := "hamsters",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
).dependsOn(macros)