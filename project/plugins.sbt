resolvers += "Flyway" at "https://flywaydb.org/repo"
resolvers += Resolver.url("scoverage-bintray", url("https://dl.bintray.com/sksamuel/sbt-plugins/"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.7")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")