name := """iPost"""

version := "1.0"

scalaVersion := "2.11.7"

lazy val scalaRequestJson = ProjectRef(file("./git-submodules/scala-request-json"), "scala-request-json")
lazy val root = Project(id = "root", base = file("./"))
  .dependsOn(scalaRequestJson)
  .aggregate(scalaRequestJson)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
