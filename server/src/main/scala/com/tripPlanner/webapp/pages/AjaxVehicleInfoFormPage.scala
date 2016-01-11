package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.shared.domain.Vehicle
import com.tripPlanner.webapp.Page
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by aabreu on 1/10/16.
  */
trait AjaxVehicleInfoFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val ajaxVehicleFormView = new AjaxVehicleFormView(Vehicle("", "", None, None, None))
        complete(ajaxVehicleFormView.apply())
      }
      }
    }
  }
}

object AjaxVehicleInfoFormPage extends AjaxVehicleInfoFormPage
