ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .enablePlugins(JacocoPlugin)
  .settings(
    name := "shipwrecker",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18" ,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"
   // Scoverage settings
    coverageEnabled := true,
    coverageMinimum := 50,             // Example: require at least 80% coverage
    coverageFailOnMinimum := true

)
