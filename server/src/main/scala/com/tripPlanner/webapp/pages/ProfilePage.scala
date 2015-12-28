package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page with LazyLogging{
  def apply()(implicit sys: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {
        logger.debug("loading profile page")
        complete(ProfileView())
      }
      }
    }
  }
}

object ProfilePage extends ProfilePage