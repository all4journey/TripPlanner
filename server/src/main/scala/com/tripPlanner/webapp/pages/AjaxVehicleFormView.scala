package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.shared.domain.Vehicle
import com.tripPlanner.webapp.{AjaxTemplate, View}

/**
  * Created by aabreu on 1/10/16.
  */
class AjaxVehicleFormView(vehicle: Vehicle) extends View {
  val myVehicle = vehicle
  def apply()(implicit ctx: RequestContext) =
    AjaxTemplate(
      Seq(jsModuleWithParams[AjaxVehicleInfoFormJs](myVehicle))
    )
}
