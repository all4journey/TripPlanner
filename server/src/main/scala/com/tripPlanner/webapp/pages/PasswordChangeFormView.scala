package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{MainTemplate, View}

/**
  * Created by aabreu on 1/10/16.
  */
class PasswordChangeFormView(emailAddress: String) extends View {
  val myEmailAddress = emailAddress
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Profile",
      footer = Seq(jsModuleWithParams[PasswordChangeFormJs](myEmailAddress))
    )
}
