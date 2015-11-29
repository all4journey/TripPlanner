import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "com.tripPlanners",
  version := "0.1.0",
  scalaVersion := "2.11.7",
  scalaJSStage in Global := FastOptStage,
  skip in packageJSDependencies := false,
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

val akkaV = "2.3.9"
val sprayV = "1.3.3"

lazy val root = (project in file("."))
  .aggregate(server, client)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1",
    "com.lihaoyi" %%% "scalatags" % "0.5.3",
    "io.surfkit" %%% "scalajs-google-maps" % "0.1-SNAPSHOT"
  ))
  .settings(
    persistLauncher in Compile := true,
    persistLauncher in Test := false
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJs)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .dependsOn(sharedJvm)
  .settings(libraryDependencies ++=Seq(
    "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0-M1",
    "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.0-M1",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M1",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  ))
  .settings(Revolver.settings,
    (resourceGenerators in Compile) <+=
      (fastOptJS in Compile in client, packageScalaJSLauncher in Compile in client, packageJSDependencies in Compile in client).
        map((f1, f2, f3) => Seq(f1.data, f2.data,f3.getAbsoluteFile)),
    watchSources <++= (watchSources in client)
  )

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    //other settings
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

Revolver.settings
