package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.shared.domain.{PlacesFormData, PersonalFormData}
import com.tripPlanner.webapp.{MainTemplate, View}

/**
  * Created by aabreu on 1/30/16.
  */

class PlacesFormView(formData: PlacesFormData) extends View {
  val myFormData = formData
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Profile",
      footer = Seq(jsModuleWithParams[PlacesFormJs](myFormData))
    )
}
