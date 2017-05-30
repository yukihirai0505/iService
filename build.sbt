name := """iPost"""

version := "1.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.yukihirai0505" % "scala-request-json_2.11" % "1.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

// when library release, it should be false
publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

organization := "com.yukihirai0505"

organizationName := "com.yukihirai0505"

organizationHomepage := Some(url("https://yukihirai0505.github.io"))

description := "A Scala library for the Instagram Private Post API"

pomExtra :=
  <url>https://github.com/yukihirai0505/iPost</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://github.com/yukihirai0505/iPost/blob/master/LICENSE.txt</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:yukihirai0505/iPost.git</url>
      <connection>scm:git:git@github.com:yukihirai0505/iPost.git</connection>
    </scm>
    <developers>
      <developer>
        <id>yukihirai0505</id>
        <name>Yuki Hirai</name>
        <url>https://yukihirai0505.github.io</url>
      </developer>
    </developers>
