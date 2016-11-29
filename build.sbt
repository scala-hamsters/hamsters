import sbt.Keys._

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "io.github.scala-hamsters",
  version := "1.1.1-SNAPSHOT",
  scalacOptions ++= Seq(),
  scalaVersion := "2.11.8",
  scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),
  publishMavenStyle := true,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.3.0",
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns),
    addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0.132" cross CrossVersion.full),
    scalacOptions += "-Xplugin-require:macroparadise"
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

lazy val macros = Project(
  "macros",
  file("macros"),
  settings = buildSettings
)

lazy val root = Project(
  "root",
  file("."),
  settings = buildSettings ++ publishSettings ++ Seq(
    name := "hamsters",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
).dependsOn(macros)
