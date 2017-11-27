import sbt.Keys._

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "io.github.scala-hamsters",
  version := "2.1.2",
  scalacOptions ++= Seq(),
  scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),
  publishMavenStyle := true,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0" % Provided,
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions ++= List("-Xplugin-require:macroparadise", "-language:higherKinds", "-language:implicitConversions", "-feature"),
  scalacOptions in(Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
  scalaCompilerBridgeSource := ("org.scala-sbt" % "compiler-interface" % "0.13.16" % "component").sources
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
        <developer>
          <id>oraclewalid</id>
          <name>Walid Chergui</name>
          <url>https://github.com/oraclewalid</url>
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

lazy val noDocFileSettings = Seq (
  sources in doc in Compile := List()
)

val hamstersSettings = buildSettings ++ publishSettings

scalaVersion in ThisBuild := "2.11.11"
crossScalaVersions in ThisBuild := Seq("2.11.11", "2.12.3")

lazy val macros = project.in(file("macros"))
  .settings(name := "macros")
  .settings(hamstersSettings)
  .settings(noDocFileSettings)

lazy val hamsters = crossProject.in(file("."))
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test")
  .settings(libraryDependencies += "org.scalamock" %%% "scalamock-scalatest-support" % "3.6.0" % "test")
  .settings(libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test")
  .settings(hamstersSettings)

lazy val hamstersJVM = hamsters.jvm.dependsOn(macros).settings(buildSettings).settings(name := "hamsters")
lazy val hamstersJS = hamsters.js.dependsOn(macros).settings(buildSettings).settings(name := "hamsters")

lazy val root = project.in(file("."))
  .aggregate(hamstersJVM, hamstersJS, macros)
  .dependsOn(hamstersJVM, hamstersJS, macros)
  .settings(hamstersSettings)
