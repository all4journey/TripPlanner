package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.shared.domain.{AddressTypePickler, PlacesFormData}
import com.all4journey.webapp.{MainTemplate, View}

/**
  * Created by aabreu on 1/30/16.
  */

class PlacesFormView(formData: PlacesFormData) extends View with AddressTypePickler {
  val myFormData = formData
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Profile",
      footer = Seq(jsModuleWithParams[PlacesFormJs](myFormData))
    )
}
