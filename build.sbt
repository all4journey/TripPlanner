import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "com.tripPlanners",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .dependsOn(sharedJvm)
  .settings(
    //other settings
  )

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1"
  ))
  .settings(
    skip in packageJSDependencies := false,
    scalaJSStage in Global := FastOptStage
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJs)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    //other settings
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js
