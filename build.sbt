ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

coverageExcludedPackages := ".*view.*"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-24-Scalata",
    libraryDependencies ++= Seq(
      // Testing
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0" % Test,
      "org.typelevel" %% "cats-effect" % "3.6.1"
    )
  )
