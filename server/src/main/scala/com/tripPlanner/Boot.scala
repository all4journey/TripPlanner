package com.tripPlanner

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import StatusCodes._
import com.tripPlanner.webapp.index.IndexPage
import tripPlanner.webapp.App

import scala.concurrent.Future



trait Routes {
  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: ArithmeticException =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(InternalServerError, entity = "Bad numbers, bad result!!!"))
        }
    }
  implicit val system = ActorSystem("akka-http-sample")

  val config = system.settings.config
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")

  sys.addShutdownHook({
    system.shutdown()
  })

  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val route =
    get {
      pathSingleSlash {
        IndexPage()
//        getFromResource("web/index-dev.html")
      } ~
        path("client-fastopt.js")(getFromResource("client-fastopt.js")) ~
        path("client-launcher.js")(getFromResource("client-launcher.js")) ~
        path("client-jsdeps.js")(getFromResource("client-jsdeps.js")) ~
        path("page2"){
          complete{
            <h1>Hello World</h1>
          }
        }
    } ~
      getFromResource("web")
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(route, interface, port)

}

object Boot extends App with Routes{


}
