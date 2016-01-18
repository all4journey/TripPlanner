package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.shared.domain.Vehicle
import com.tripPlanner.webapp.{MainTemplate, View}

/**
  * Created by aabreu on 1/10/16.
  */
class VehicleFormView(vehicles: Seq[Vehicle]) extends View {
  val myVehicles = vehicles
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Edit Profile",
      footer = Seq(jsModuleWithParams[VehicleInfoFormJs](myVehicles))
    )

}
