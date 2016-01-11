package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration._

import scala.concurrent.Await

/**
  * Created by aabreu on 1/10/16.
  */
trait AjaxPersonalInfoFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val states = ProfileLogic.getStates

        val myList = Await.result(states, 10 seconds) //TODO: look into removing Await

        val ajaxPersonalInfoFormView = new AjaxPersonalInfoFormView(myList)
        complete(ajaxPersonalInfoFormView.apply())
      }
      }
    }
  }
}

object AjaxPersonalInfoFormPage extends AjaxPersonalInfoFormPage
