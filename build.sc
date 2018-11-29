import ammonite.ops._
import mill._
import mill.modules.Jvm.createJar
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.publish._

object hamster extends Cross[HamsterModule]("2.11.11", "2.12.4")

class HamsterModule(val scalaCrossVersion: String) extends Module {
  override def millSourcePath = super.millSourcePath / up / up

  trait HamsterModule extends SbtModule with PublishModule {

    def scalaVersion = scalaCrossVersion

    override def scalacOptions = Seq("-Xplugin-require:macroparadise", "-language:higherKinds", "-language:implicitConversions", "-feature")

    def publishVersion = "2.6.0"

    def pomSettings = PomSettings(
      description = "A mini Scala utility library",
      organization = "io.github.scala-hamsters",
      url = "https://github.com/scala-hamsters/hamsters",
      licenses = Seq(License.`Apache-2.0`),
      versionControl = VersionControl.github("scala-hamsters", "hamsters"),
      developers = Seq(
        Developer("loicdescotte", "Loïc Descotte", "http://loicdescotte.github.io"),
        Developer("dgouyette", "Damien Gouyette", "http://www.Cestpasdur.com"),
        Developer("oraclewalid", "Walid Chergui", "https://github.com/oraclewalid")
      )
    )

    object test extends Tests {
      override def scalaVersion = scalaCrossVersion

      override def ivyDeps = Agg(
        ivy"org.scalatest::scalatest:3.0.1",
        ivy"org.scalamock::scalamock-scalatest-support:3.6.0",
        ivy"org.scalacheck::scalacheck:1.13.4",
        ivy"ca.mrvisser::sealerate:0.0.5"
      )

      def testFrameworks = Seq("org.scalatest.tools.Framework")
    }

  }

  object jvm extends HamsterMacroParadiseModule with TransitiveJar {
    override def moduleDeps = Seq(shared)

    override def artifactName = "hamsters"
  }

  object macros extends HamsterMacroParadiseModule {
    override def millSourcePath = super.millSourcePath / 'shared

    override def artifactName = "macros"
  }

  object metas extends HamsterMetaParadiseModule {
    override def millSourcePath = super.millSourcePath

    override def sources = T.sources(
      millSourcePath / 'jvm / 'src / 'main,
      millSourcePath / 'shared / 'src / 'main
    )

    override def artifactName = "metas"
  }

  object shared extends HamsterMetaParadiseModule {
    override def moduleDeps = Seq(macros, metas)
  }

  object macrosJS extends HamsterMacroParadiseModule with HamsterScalaJSModule {
    override def millSourcePath = super.millSourcePath / up

    override def sources = T.sources(
      millSourcePath / 'macros / 'js / 'src / 'main,
      millSourcePath / 'macros / 'shared / 'src / 'main
    )

    override def artifactName = "macros"
  }

  object metasJS extends HamsterMetaParadiseModule with HamsterScalaJSModule {
    override def millSourcePath = super.millSourcePath / up

    override def sources = T.sources(
      millSourcePath / 'metas / 'js / 'src / 'main,
      millSourcePath / 'metas / 'shared / 'src / 'main
    )

    override def artifactName = "metas"
  }

  object js extends HamsterModule with HamsterScalaJSModule with TransitiveJar {
    override def moduleDeps = Seq(macrosJS, metasJS)

    override def artifactName = "hamsters"
  }

  trait HamsterMetaParadiseModule extends HamsterModule {
    override def repositories() = super.repositories ++ Seq(
      coursier.ivy.IvyRepository.parse("https://dl.bintray.com/scalameta/maven/", dropInfoAttributes = true).toOption.get
    )

    override def ivyDeps = Agg(
      ivy"org.scalameta::scalameta:1.8.0"
    )

    override def scalacPluginIvyDeps = super.scalacPluginIvyDeps() ++ Agg(
      ivy"org.scalameta:::paradise:3.0.0-M10"
    )
  }

  trait HamsterMacroParadiseModule extends HamsterModule {
    override def ivyDeps = Agg(
      ivy"org.scala-lang:scala-reflect:$scalaCrossVersion"
    )

    override def scalacPluginIvyDeps = super.scalacPluginIvyDeps() ++ Agg(
      ivy"org.scalamacros:::paradise:2.1.0"
    )
  }

  trait HamsterScalaJSModule extends ScalaJSModule {
    def scalaJSVersion = "0.6.23"
  }

  trait TransitiveJar extends JavaModule {
    override def jar = T {
      createJar(
        localClasspath().map(_.path).filter(exists) ++ transitiveLocalClasspath().map(_.path).filter(exists),
        mainClass()
      )
    }
  }

}

object release extends Module {

  def prepare(releaseVersion: String) = T.command {
    updateReadme(releaseVersion, releaseVersion) // release version and documentation version are the same
    updateREPLscript(releaseVersion)
    tag(releaseVersion)
  }

  private def updateReadme(releaseVersion: String, documentationVersion: String): Unit = {
    val readme = pwd / "README.adoc"
    val content = read ! readme
    write.over(readme, content
      .replaceAll(":release-version: ([0-9\\.]+)", s":release-version: $releaseVersion")
      .replaceAll(":documentation-version: ([0-9\\.]+)", s":documentation-version: $documentationVersion"))
  }

  private def updateREPLscript(releaseVersion: String): Unit = {
    val script = pwd / 'scripts / "try-hamsters.sh"
    val content = read ! script
    write.over(script, content
      .replaceAll("io\\.github\\.scala-hamsters:hamsters_2\\.12:([0-9\\.]+)", s"io.github.scala-hamsters:hamsters_2.12:$releaseVersion"))
  }

  private def tag(releaseVersion: String): Unit = {
    %%('git, 'commit, "-am", s"Release $releaseVersion")(pwd)
    %%('git, 'tag, releaseVersion)(pwd)
  }
}