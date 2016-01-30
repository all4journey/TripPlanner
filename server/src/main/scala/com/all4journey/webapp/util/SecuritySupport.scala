package com.all4journey.webapp.util

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import com.typesafe.config.ConfigFactory

/**
  * Created by rjkj on 1/3/16.
  */
trait SecuritySupport {
  lazy val allowedOriginHeader = {
    val config = ConfigFactory.load()
    val sAllowedOrigin = config.getString("security.allowed-origin")
    if (sAllowedOrigin.equals("*"))
      `Access-Control-Allow-Origin`.*
    else
      `Access-Control-Allow-Origin`(HttpOrigin(sAllowedOrigin))
  }

  private def addAccessControlHeaders(): Directive0 = {
    mapResponseHeaders { headers =>
      allowedOriginHeader +:
        `Access-Control-Allow-Credentials`(true) +:
        `Access-Control-Allow-Headers`("Token", "Content-type", "X-Requested-With") +:
        headers
    }
  }

  private def preFlightRequestHandler: Route = options {
    complete(HttpResponse(200).withHeaders(
      `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)
    ))
  }

  /**
    * Adds security headers to a route
    * @param r - Route to be wrapped
    * @return
    */
  def securityHandler(r: Route): Route = addAccessControlHeaders() {
    preFlightRequestHandler ~ r
  }
}

