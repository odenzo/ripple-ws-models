import sbt.Keys.resolvers
import MyCompileOptions._

ThisBuild / organization := "com.odenzo"
ThisBuild / name         := "ripple-ws-models"

transitiveClassifiers := Seq("sources")

lazy val supportedScalaVersions = List("2.13.3")
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
    }),
    libraryDependencies += (CrossVersion.partialVersion(scalaVersion.value) match { // Partial function?
      case Some((2, 12)) => compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
      case _             => "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
    })
    // libraryDependencies += compilerPlugin("io.tryp" % "splain" % "0.4.1" cross CrossVersion.patch)
  )

lazy val commonSettings = Seq(
  libraryDependencies ++= libs ++ lib_circe ++ lib_cats ++ lib_spire ++ lib_monocle ++ lib_scribe,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.defaultLocal,
    Resolver.bintrayRepo("odenzo", "maven"),
    Resolver.jcenterRepo
  )
)

val circeVersion             = "0.13.0"
val circeGenericExtraVersion = "0.13.0"
val catsVersion              = "2.1.1"
val catsEffectVersion        = "2.1.4"
val spireVersion             = "0.17.0-RC1"
val scribeVersion            = "2.7.12"
val scalaTestVersion         = "3.2.0"
val scalaCheckVersion        = "1.14.3"
val enumeratumVersion        = "1.5.13"
val enumeratumCirceVersion   = "1.6.1"
val monocleVersion           = "2.0.5"
val pprintVersion            = "0.5.9"

val libs = Seq(
  "com.lihaoyi"    %% "pprint"     % pprintVersion,
  "org.scalatest"  %% "scalatest"  % scalaTestVersion % Test,
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
)

/** JSON Libs == Circe and Associated Support Libs */
val lib_circe =
  Seq(
    "io.circe" %% "circe-core"           % circeVersion,
    "io.circe" %% "circe-generic"        % circeVersion,
    "io.circe" %% "circe-parser"         % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeGenericExtraVersion,
    "io.circe" %% "circe-optics"         % circeVersion,
    "io.circe" %% "circe-literal"        % circeVersion % Test,
    //"io.circe"     %% "circe-derivation"     % circeVersion,
    "com.beachape" %% "enumeratum"       % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion,
    "io.circe"     %% "circe-golden"     % "0.2.1" % Test
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
  "org.typelevel" %% "cats-core"   % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion
)

val lib_scribe = Seq("com.outr" %% "scribe" % scribeVersion)
