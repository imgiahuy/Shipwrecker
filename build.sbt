ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .enablePlugins(JacocoCoverallsPlugin)
  .settings(
    name := "shipwrecker",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    jacocoCoverallsServiceName := "github-actions", 
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
)
jacocoReportSettings := JacocoReportSettings()
  .withThresholds(
    JacocoThresholds(
      branch = 70
    )
  )
