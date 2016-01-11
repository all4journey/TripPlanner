package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{AjaxTemplate, View}

/**
  * Created by aabreu on 1/10/16.
  */
class AjaxPasswordChangeFormView(emailAddress: String) extends View {
  val myEmailAddress = emailAddress
  def apply()(implicit ctx: RequestContext) =
    AjaxTemplate(
      Seq(jsModuleWithParams[AjaxPasswordChangeFormJs](myEmailAddress))
    )
}
