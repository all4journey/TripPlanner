package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.all4journey.shared.domain.Vehicle
import com.all4journey.webapp.Page
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by aabreu on 1/10/16.
  */
trait VehicleInfoFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val vehicleFormView = new VehicleFormView(Seq[Vehicle]())
        complete(vehicleFormView.apply())
      }
      }
    }
  }
}

object VehicleInfoFormPage extends VehicleInfoFormPage
