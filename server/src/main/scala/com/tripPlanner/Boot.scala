package com.tripPlanner

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.server.Directives._
import akka.stream._

/**
  * Created by rjkj on 11/21/15.
  */
trait Routes {
  implicit val system = ActorSystem("akka-http-sample")

  val config = system.settings.config
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")

  sys.addShutdownHook({ system.shutdown() })

  implicit val materializer = ActorFlowMaterializer()

  import system.dispatcher

  val route =
    get{
      pathSingleSlash {
        getFromResource("web/index-dev.html")
      }~
        path("client-fastopt.js")(getFromResource("client-fastopt.js")) ~
        path("client-launcher.js")(getFromResource("client-launcher.js")) ~
        path("client-jsdeps.js")(getFromResource("client-jsdeps.js"))
    } ~
      getFromResource("web")
  val serverBinding = Http(system).bind(interface = interface, port = port)

  serverBinding.startHandlingWith(route)
}

object Boot extends App with Routes {


}
