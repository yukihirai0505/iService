name := """common"""

scalaVersion := "2.11.7"

lazy val common = project in file(".")

libraryDependencies ++= Seq(
  "com.yukihirai0505" % "scala-request-json_2.11" % "1.3"
)

