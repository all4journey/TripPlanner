package com.tripPlanner.webapp.profile

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page

/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page {
  def apply()(implicit sys:ActorSystem, mat:Materializer) = pathEnd{
    get{
      extractRequestContext { implicit ctx =>
        complete(ProfileView())
      }
    }
  }
}

object ProfilePage extends ProfilePage