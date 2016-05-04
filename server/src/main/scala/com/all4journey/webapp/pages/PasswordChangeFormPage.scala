package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.UserContext
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by aabreu on 1/10/16.
  */
trait PasswordChangeFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {



        val ajaxPasswordChangeFormView = new PasswordChangeFormView("token", UserContext.getCurrentUser.email)
        complete(ajaxPasswordChangeFormView.apply())
      }
      }
    }
  }
}

object PasswordChangeFormPage extends PasswordChangeFormPage
