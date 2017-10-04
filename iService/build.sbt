name := """iService"""

version := "2.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  // Log
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.yukihirai0505" % "scala-request-json_2.11" % "1.4",
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

description := "A Scala library for Instagram"

pomExtra :=
  <url>https://github.com/yukihirai0505/iService</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://github.com/yukihirai0505/iService/blob/master/LICENSE.txt</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:yukihirai0505/iService.git</url>
      <connection>scm:git:git@github.com:yukihirai0505/iService.git</connection>
    </scm>
    <developers>
      <developer>
        <id>yukihirai0505</id>
        <name>Yuki Hirai</name>
        <url>https://yukihirai0505.github.io</url>
      </developer>
    </developers>
