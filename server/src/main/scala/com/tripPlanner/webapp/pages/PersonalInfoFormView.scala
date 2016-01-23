package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{MainTemplate, View}
import com.tripPlanner.shared.domain.PersonalFormData

/**
  * Created by aabreu on 1/10/16.
  */
class PersonalInfoFormView(formData: PersonalFormData) extends View {
  val myFormData = formData
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Edit Profile",
      footer = Seq(jsModuleWithParams[PersonalInfoFormJs](myFormData))
    )
}
