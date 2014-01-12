import sbt._
import sbt.{Build => SbtBuild}
import sbt.Keys._


object Build extends SbtBuild {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.monifu",
    version := "0.1",
    scalaVersion := "2.10.3",

    scalacOptions ++= Seq(
      "-unchecked", "-deprecation", "-feature", "-target:jvm-1.6"
    ),
    resolvers ++= Seq(
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      Resolver.sonatypeRepo("releases")
    ),

    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.10" % "test",
      "org.scalatest" %% "scalatest" % "1.9.1" % "test"
    ),

    addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0-M2" cross CrossVersion.full),

    licenses := Seq("ALv2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("http://www.monifu.org/")),
    pomExtra := (
      <scm>
        <url>git@github.com:alexandru/monifu.git</url>
        <connection>scm:git:git@github.com:alexandru/monifu.git</connection>
      </scm>
      <developers>
        <developer>
          <id>alex_ndc</id>
          <name>Alexandru Nedelcu</name>
          <url>https://www.bionicspirit.com/</url>
        </developer>
      </developers>)
  )

  lazy val root: Project = Project(
    id = "monifu",
    base = file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core
    )
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    id = "monifu-macros",
    base = file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _))
  )

  lazy val core: Project = Project(
    id = "monifu-core",
    base = file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}