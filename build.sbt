ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .enablePlugins(JacocoCoverallsPlugin)
  .settings(
    name := "shipwrecker",
        libraryDependencies += "org.scalafx" %% "scalafx" % "22.0.0-R33",
        libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
        libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
        jacocoCoverallsServiceName := "github-actions",
        jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
        jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
        jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN"),
        libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
        libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
        libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
        libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.5"

)
