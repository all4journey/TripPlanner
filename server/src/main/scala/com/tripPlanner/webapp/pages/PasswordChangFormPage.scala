package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by aabreu on 1/10/16.
  */
trait PasswordChangFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val ajaxPasswordChangeFormView = new PasswordChangeFormView("a.a@gmail.com")
        complete(ajaxPasswordChangeFormView.apply())
      }
      }
    }
  }
}

object PasswordChangFormPage extends PasswordChangFormPage
