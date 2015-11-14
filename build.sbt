import sbt.Keys._
import sbt._

enablePlugins(ScalaJSPlugin)

scalaJSStage in Global := FastOptStage

skip in packageJSDependencies := false

lazy val commonSettings = Seq(
  organization := "com.tripPlanners",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "travelPlanner"
  )

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "be.doeraene" %%% "scalajs-jquery" % "0.8.1"
)