import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val globalSettings = Seq(
  organization := "io.github.scala-hamsters",
  version := "3.1.0",
  scalacOptions ++= Seq("-language:implicitConversions", "-feature", "-deprecation"),
  publishMavenStyle := true
)

val noPublishSettings = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {},
)

lazy val publishSettings = Seq(
  pomExtra :=
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
          <id>hamstersTeam</id>
          <name>Hamsters Team</name>
          <url>https://github.com/scala-hamsters/hamsters/graphs/contributors</url>
        </developer>
      </developers>
)

val hamstersSettings = globalSettings ++ publishSettings

ThisBuild / scalaVersion := "3.3.8"
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.toLowerCase.endsWith("snapshot"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("staging" at nexus + "service/local/staging/deploy/maven2")
}

lazy val hamsters = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.2.19" % Test,
    "org.scalamock" %%% "scalamock" % "6.0.0" % Test,
    "org.scalacheck" %%% "scalacheck" % "1.18.1" % Test
  ))
  .settings(hamstersSettings)

lazy val hamstersJVM = hamsters.jvm.settings(name := "hamsters")
lazy val hamstersJS = hamsters.js.settings(name := "hamsters")

lazy val root = project.in(file("."))
  .aggregate(hamstersJVM, hamstersJS)
  .dependsOn(hamstersJVM, hamstersJS)
  .settings(hamstersSettings)
  .settings(noPublishSettings)
