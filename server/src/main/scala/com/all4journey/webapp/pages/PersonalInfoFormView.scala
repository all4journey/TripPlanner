package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.webapp.{MainTemplate, View}
import com.all4journey.shared.domain.{AddressTypePickler, PersonalFormData}

/**
  * Created by aabreu on 1/10/16.
  */
class PersonalInfoFormView(formData: PersonalFormData) extends View with AddressTypePickler {
  val myFormData = formData
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Profile",
      footer = Seq(jsModuleWithParams[PersonalInfoFormJs](myFormData))
    )
}
