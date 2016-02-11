package com.all4journey

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.all4journey.webapp.Routes

import scala.concurrent.Future


// $COVERAGE-OFF$
object Boot extends App{

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

  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(Routes(), interface, port)
}
// $COVERAGE-ON$