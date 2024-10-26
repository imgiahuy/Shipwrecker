ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "shipwrecker",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    // Scoverage settings
    coverageEnabled := true,           // Ensure proper commas and no parentheses are missing
    coverageMinimum := 80,             // Example: require at least 80% coverage
    coverageFailOnMinimum := true
  )

