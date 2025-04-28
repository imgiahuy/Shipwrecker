ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.1"

lazy val dependencies = Seq (
        libraryDependencies += "org.scalafx" %% "scalafx" % "22.0.0-R33",
        libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
        libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
        jacocoCoverallsServiceName := "github-actions",
        jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
        jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
        jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN"),
        libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
        libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
        libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
        libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.6" ,
        libraryDependencies +=  "org.mockito" % "mockito-core" % "5.14.2" % Test,
        libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test
)

lazy val root = project
  .in(file("."))
  .settings(
      name := "shipwrecker",
      dependencies
  ).dependsOn(
      util,
      controller,
      gui,
      tui,
      model,
      fileIO,
      launcher
  ).aggregate(
      util,
      controller,
      gui,
      tui,
      model,
      fileIO,
      launcher
  )

lazy val util = project
    .in(file("util"))
    .settings(
      name := "util",
      dependencies
  )

lazy val controller = project
  .in(file("controller"))
  .settings(
    name := "controller",
    dependencies
  ).dependsOn(model, util, fileIO)

lazy val model = project
  .in(file("model"))
  .settings(
    name := "model",
    dependencies
  ).dependsOn(util)

lazy val fileIO = project
  .in(file("fileIO"))
  .settings(
    name := "fileIO",
    dependencies
  ).dependsOn(model)

lazy val launcher = project
  .in(file("launcher"))
  .settings(
    name := "launcher",
    dependencies
  ).dependsOn(model, util, fileIO, tui, gui, controller)

lazy val gui = project
  .in(file("gui"))
  .settings(
    name := "gui",
    dependencies
  ).dependsOn(util, controller)

lazy val tui = project
  .in(file("tui"))
  .settings(
    name := "tui",
    dependencies
  ).dependsOn(util, controller)





