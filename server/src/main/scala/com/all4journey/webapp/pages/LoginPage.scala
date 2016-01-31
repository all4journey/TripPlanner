package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpHeader, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.all4journey.domain.security.AuthDao
import com.all4journey.shared.domain.security.LoginCredentials
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.{Pickle, Unpickle}
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.Success

/**
  * Created by rjkj on 1/31/16.
  */
class LoginPage extends Page with LazyLogging{
  override def apply()(implicit sys: ActorSystem, mat: Materializer): Route = pathEnd {
    get {
      extractRequestContext {implicit ctx => {
        complete(LoginView())
      }}
    } ~
    post {
      extractRequestContext {implicit ctx =>
        entity(as[String]) {loginJsonPlayload =>
          Unpickle[LoginCredentials].fromString(loginJsonPlayload) match {
            case Success(loginCredentials) =>
              logger.info(s"username: ${loginCredentials.emailAddress}, password: ${loginCredentials.password}")
              val future = login(loginCredentials) map {
                case Some(token) => complete(StatusCodes.OK)
                case None => complete(StatusCodes.Unauthorized)
              }
              Await.result(future, 5 seconds)
//              complete(StatusCodes.OK)
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

  def login(loginCredentials: LoginCredentials) = {
    val dao = AuthDao(DomainSupport.db)
    val future = dao.signIn(loginCredentials.emailAddress, loginCredentials.password)
    future
  }
}

object LoginPage extends LoginPage
