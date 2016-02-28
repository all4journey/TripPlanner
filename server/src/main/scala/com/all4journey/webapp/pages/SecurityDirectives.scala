package com.all4journey.webapp.pages

import akka.http.scaladsl.server.directives.{FutureDirectives, RouteDirectives, BasicDirectives, HeaderDirectives}
import akka.http.scaladsl.server._
import com.all4journey.domain.security.AuthDao
import com.all4journey.shared.domain.User
import com.all4journey.webapp.util.DomainSupport
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by rjkj on 2/28/16.
  */
trait SecurityDirectives {
  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._
  import FutureDirectives._

  def authenticate: Directive1[User] = {
    headerValueByName("Token").flatMap { token =>
      onSuccess(AuthDao(DomainSupport.db).authenticate(token)).flatMap {
        case Some(user) => provide(user)
        case None       => reject
      }
    }
  }
}
