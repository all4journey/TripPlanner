package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.shared.domain.Vehicle
import com.all4journey.webapp.{MainTemplate, View}

/**
  * Created by aabreu on 1/10/16.
  */
class VehicleFormView(vehicles: Seq[Vehicle]) extends View {
  val myVehicles = vehicles
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Edit Profile",
      footer = Seq(jsModuleWithParams[VehicleInfoFormJs]("token", myVehicles))
    )

}
