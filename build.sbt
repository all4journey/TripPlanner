import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "com.tripPlanners",
  version := "0.1.0",
  scalaVersion := "2.11.7",
  scalaJSStage in Global := FastOptStage,
  skip in packageJSDependencies := false,
  resolvers ++= Seq(
    "Typesafe repository snapshots"   at "http://repo.typesafe.com/typesafe/snapshots/",
    "Typesafe repository releases"    at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype repo"                   at "https://oss.sonatype.org/content/groups/scala-tools/",
    "Sonatype releases"               at "https://oss.sonatype.org/content/repositories/releases",
    "Sonatype snapshots"              at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype staging"                at "http://oss.sonatype.org/content/repositories/staging",
    "Java.net Maven2 Repository"      at "http://download.java.net/maven/2/",
    "Twitter Repository"              at "http://maven.twttr.com",
    Resolver.bintrayRepo("websudos", "oss-releases")
  )
)

lazy val jsCommonLibs = Seq(

)

val akkaV = "2.3.9"
val sprayV = "1.3.3"
val scalaJsV = "0.6.5"
val PhantomVersion = "1.12.2"

lazy val root = (project in file("."))
  .aggregate(server, client, domain, sharedJvm, sharedJs)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1",
    "com.lihaoyi" %%% "scalatags" % "0.5.3",
    "com.github.japgolly.scalacss" %%% "core" % "0.3.1",
    "io.surfkit" %%% "scalajs-google-maps" % "0.1-SNAPSHOT"),
    jsDependencies += "org.webjars.bower" % "bootstrap" % "3.3.4" / "bootstrap.js" commonJSName "bootstrap"
  )
  .settings(
    persistLauncher in Compile := true,
    persistLauncher in Test := false
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJs)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .dependsOn(sharedJvm)
  .dependsOn(client)
  .settings(libraryDependencies ++=Seq(
    "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0-M1",
    "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.0-M1",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M1",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "com.lihaoyi" %% "scalatags" % "0.5.3",
    "com.github.japgolly.scalacss" %% "core" % "0.3.1",
    "org.webjars" % "webjars-locator" % "0.23"
  ))
  .settings(Revolver.settings,
    (resourceGenerators in Compile) <+=
      (fastOptJS in Compile in client, packageScalaJSLauncher in Compile in client, packageJSDependencies in Compile in client).
        map((f1, f2, f3) => Seq(f1.data, f2.data,f3.getAbsoluteFile)),
    watchSources <++= (watchSources in client)
  )

lazy val domain = (project in file("domain"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.websudos" %% "phantom-dsl" % PhantomVersion,
    "com.websudos" %% "phantom-testkit" % PhantomVersion % "test, provided"
  ))
  .dependsOn(sharedJvm)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.benhutchison" %%% "prickle" % "1.1.10",
      "org.scala-js" %% "scalajs-stubs" % scalaJsV
    )

    //other settings
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

Revolver.settings
