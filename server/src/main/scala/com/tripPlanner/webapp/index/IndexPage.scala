package com.tripPlanner.webapp.index

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page

/**
  * Created by rjkj on 12/5/15.
  */
trait IndexPage extends Page {
  def apply()(implicit sys:ActorSystem, mat:Materializer) = pathEnd{
    get{
      complete(IndexView())
    }
  }
}

object IndexPage extends IndexPage
