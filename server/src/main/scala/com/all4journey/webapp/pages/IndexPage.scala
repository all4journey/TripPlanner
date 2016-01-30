package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.all4journey.webapp.Page

/**
  * Created by rjkj on 12/5/15.
  */
trait IndexPage extends Page {
  def apply()(implicit sys:ActorSystem, mat:Materializer) = pathEnd{
    get{
      extractRequestContext { implicit ctx =>
        complete(IndexView())
      }
    }
  }
}

object IndexPage extends IndexPage
