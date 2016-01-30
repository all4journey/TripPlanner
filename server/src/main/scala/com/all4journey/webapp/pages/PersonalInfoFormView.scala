package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.webapp.{MainTemplate, View}
import com.all4journey.shared.domain.PersonalFormData

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
