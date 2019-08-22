//
//bintrayRepository := (if (isSnapshot.value) "sbt-plugin-snapshots" else "sbt-plugins")
//
//bintrayOrganization in bintray := None
//
//bintrayReleaseOnPublish := isSnapshot.value
enablePlugins(JavaAppPackaging)
bintrayPackage in ThisBuild := "ripple-ws-models"

licenses in ThisBuild += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage in ThisBuild := Some(url("https://github.com/odenzo/ripple-ws-models"))

credentials in ThisBuild += Credentials(Path.userHome / ".sbt" / ".credentials")
credentials ++= (
  for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    username,
    password
  )
).toSeq

//publishArtifact in Test := true // to add the tests JAR
publishArtifact in Test              := true
publishMavenStyle in ThisBuild       := true
bintrayOrganization in ThisBuild     := Some("odenzooss")
bintrayReleaseOnPublish in ThisBuild := false
bintrayPackageLabels in ThisBuild    := Seq("Ripple", "xrpl", "scala")
