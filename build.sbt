import sbt.Keys.resolvers
import MyCompileOptions._

//resolvers += Resolver.sonatypeRepo("releases")
//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

ThisBuild / organization := "com.odenzo"
ThisBuild / name         := "ripple-ws-models"

transitiveClassifiers := Seq("sources")

lazy val supportedScalaVersions = List("2.13.0", "2.12.9")
ThisBuild / scalaVersion := supportedScalaVersions.head

Test / fork              := true
Test / logBuffered       := false
Test / parallelExecution := false

lazy val root = (project in file("."))
  .aggregate(wsmodels)
  .settings(
    crossScalaVersions := Nil,
    publish / skip     := true
  )
lazy val wsmodels = (project in file("."))
  .settings(
    name := "ripple-ws-models",
    commonSettings,
    crossScalaVersions := supportedScalaVersions,
    scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 12 => optsV12 ++ warningsV12 ++ lintersV12
      case Some((2, n)) if n >= 13 => optsV13 ++ warningsV13 ++ lintersV13
      case _                       => Seq("-Yno-adapted-args")
    })
  )

lazy val commonSettings = Seq(
  libraryDependencies ++= libs ++ lib_circe ++ lib_cats ++ lib_spire ++ lib_monocle ++ lib_scribe,
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.defaultLocal,
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("odenzo", "maven")
  )
)

val circeVersion           = "0.12.0-RC3"
val catsVersion            = "2.0.0-RC1"
val catsEffectVersion      = "2.0.0-RC1"
val spireVersion           = "0.17.0-M1"
val scribeVersion          = "2.7.9"
val scalaTestVersion       = "3.0.8"
val scalaCheckVersion      = "1.14.0"
val enumeratumVersion      = "1.5.13"
val enumeratumCirceVersion = "1.5.21"
val monocleVersion         = "1.6.0" // 1.5.0-cats based on cats 1.0.x

val libs = Seq(
  "org.scalatest"  %% "scalatest"  % scalaTestVersion  % Test,
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
)

/** JSON Libs == Circe and Associated Support Libs */
val lib_circe =
  Seq(
    "io.circe" %% "circe-core"           % circeVersion,
    "io.circe" %% "circe-generic"        % circeVersion,
    "io.circe" %% "circe-parser"         % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion,
    //"io.circe"     %% "circe-derivation"     % circeVersion,
    "com.beachape" %% "enumeratum"       % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion
  )

val lib_monocle = Seq(
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-law"   % monocleVersion % "test"
)

/** Currently this is only for the binary serialization */
val lib_spire = Seq(
  "org.typelevel" %% "spire"        % spireVersion,
  "org.typelevel" %% "spire-extras" % spireVersion
)

val lib_cats = Seq(
  "org.typelevel" %% "cats-core"   % catsVersion, // Cats is pulled in via Circe for now
  "org.typelevel" %% "cats-effect" % catsEffectVersion
)

val lib_scribe = Seq("com.outr" %% "scribe" % scribeVersion)
