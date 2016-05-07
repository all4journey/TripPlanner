import sbt.Keys._
import sbt._
import NativePackagerHelper._

lazy val commonSettings = Seq(
  organization := "com.all4journey",
  version := "0.1.0",
  scalaVersion := "2.11.7",
  scalaJSStage in Global := FastOptStage,
  skip in packageJSDependencies := false,
  coverageEnabled.in(Test, test) := true,
  coverageExcludedFiles.in(Test, test) := "*Tables.*",
  resolvers ++= Seq(
    "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype repo" at "https://oss.sonatype.org/content/groups/scala-tools/",
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype staging" at "http://oss.sonatype.org/content/repositories/staging",
    "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
    "Twitter Repository" at "http://maven.twttr.com",
    "Flyway" at "https://flywaydb.org/repo",
    Resolver.bintrayRepo("websudos", "oss-releases")
  )
)

lazy val commonTestDeps = Seq(
  "org.scalatest" %% "scalatest" % Settings.versions.scalaTest % "test"
)

lazy val all4journey = (project in file("."))
  .aggregate(server, client, domain, sharedJvm, sharedJs)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Settings.clientDependencies.value,
    jsDependencies ++= Settings.jsDependencies.value,
    jsDependencies += RuntimeDOM % "test",
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    requiresDOM := true,
    coverageEnabled.in(Test, test) := false
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJs)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .enablePlugins(JavaServerAppPackaging)
  .dependsOn(sharedJvm, client, domain)
  .settings(libraryDependencies ++= Settings.serverDependencies.value,
    Revolver.settings,
    (resourceGenerators in Compile) <+=
      (fastOptJS in Compile in client, packageScalaJSLauncher in Compile in client, packageJSDependencies in Compile in client).
        map((f1, f2, f3) => Seq(f1.data, f2.data, f3.getAbsoluteFile)),
    watchSources <++= (watchSources in client)
  )
  .settings(
      mainClass in Compile := Some("com.tripPlanner.Boot"),
      mappings in Universal ++= {contentOf("/src/main/resources").toMap.mapValues("config/" + _).toSeq},
      scriptClasspath := Seq("../config/") ++ scriptClasspath.value
      )

lazy val domain = (project in file("domain"))
  .settings(commonSettings: _*)
//  .settings(flywayBaseSettings: _*)
  .settings(
    libraryDependencies ++= Settings.domainDependencies.value,
    libraryDependencies ++= commonTestDeps,
    flywayUrl := "jdbc:mysql://127.0.0.1:3306/all4journey",
    flywayUser := "root",
    flywayLocations += "db/migration",
    flywayPassword := sys.props.getOrElse("flyway_password", "password1"),
    coverageExcludedFiles := "Tables.*",
    slick <<= slickCodeGenTask
    //    ,sourceGenerators in Compile <+= slickCodeGenTask
  )
  .dependsOn(sharedJvm)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Settings.sharedDependencies.value
  ).jsSettings(coverageEnabled:=false)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "generated").getPath
  val url = "jdbc:mysql://localhost:3306/all4journey?user=root&password=password1"
  val jdbcDriver = "com.mysql.jdbc.Driver"
  val slickDriver = "slick.driver.MySQLDriver"
  val pkg = "com.all4journey.domain.generated"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  val fname = s"$outputDir/com/all4journey/domain/generated/Tables.scala"
  Seq(file(fname))
}

Revolver.settings
