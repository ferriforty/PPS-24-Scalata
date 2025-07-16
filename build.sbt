ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-24-Scalata",
    Compile / run / fork          := true,
    Compile / run / connectInput  := true,
    Global / useSuperShell        := false ,
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    coverageExcludedPackages      := ".*view.*",

    //JLINE APP
    //assembly / mainClass := Some("scalata.infrastructure.view.terminal.jline.JLineApp"),

    // CLI APP
    assembly / mainClass := Some("scalata.infrastructure.view.terminal.cli.CliApp"),

    libraryDependencies ++= Seq(
      "org.jline" % "jline" % "3.30.4",
      "it.unibo.alice.tuprolog" % "tuprolog" % "3.3.0",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0" % Test,
      "org.typelevel" %% "cats-effect" % "3.6.2"
    )
  )
