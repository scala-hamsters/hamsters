name := "hamsters"

organization := "io.github.scala-hamsters"

version := "1.0.8-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

scalacOptions in (Compile,doc) := Seq("-groups", "-implicits")
