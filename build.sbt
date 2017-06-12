import sbt.Keys._

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "io.github.scala-hamsters",
  version := "1.3.2-SNAPSHOT",
  scalacOptions ++= Seq(),
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.11.11", "2.12.1"),
  scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),
  publishMavenStyle := true,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.7.0",
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M8" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in(Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  sources in(Compile, doc) := Nil, // macroparadise doesn't work with scaladoc yet.
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")
)

lazy val publishSettings = Seq(
  pomExtra := (
    <url>https://github.com/scala-hamsters/hamsters</url>
    <licenses>
      <license>
        <name>Apache 2.0</name>
        <url>http://www.apache.org/licenses/</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:scala-hamsters/hamsters.git</url>
      <connection>scm:git@github.com:scala-hamsters/hamsters.git</connection>
    </scm>
    <developers>
      <developer>
        <id>loicdescotte</id>
        <name>Loïc Descotte</name>
        <url>http://loicdescotte.github.io/</url>
      </developer>
      <developer>
        <id>dgouyette</id>
        <name>Damien Gouyette</name>
        <url>http://www.Cestpasdur.com/</url>
      </developer>
    </developers>
  ),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.toLowerCase.endsWith("snapshot"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("staging" at nexus + "service/local/staging/deploy/maven2")
  }
)
scalaVersion in ThisBuild := "2.11.11"


lazy val macros = crossProject.in(file("macros"))
  .settings(name := "macros")
  .settings(buildSettings)

lazy val macrosJVM = macros.jvm
lazy val macrosJS = macros.js


lazy val hamsters = crossProject.in(file("."))
  .settings(name := "hamsters")
  .settings(buildSettings ++ publishSettings)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test")

lazy val hamstersJVM = hamsters.jvm.dependsOn(macrosJVM)
lazy val hamstersJS = hamsters.js.dependsOn(macrosJS)



lazy val root = project.in(file("."))
  .aggregate(hamstersJVM, hamstersJS)
  .dependsOn(hamstersJVM, hamstersJS)

