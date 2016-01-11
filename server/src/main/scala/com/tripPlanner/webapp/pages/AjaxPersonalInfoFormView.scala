package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{AjaxTemplate, MainTemplate, View}
import com.tripPlanner.shared.domain.State

/**
  * Created by aabreu on 1/10/16.
  */
class AjaxPersonalInfoFormView(states: Seq[State]) extends View {
  val myStates = states
  def apply()(implicit ctx: RequestContext) =
    AjaxTemplate(
      footer = Seq(jsModuleWithParams[AjaxPersonalInfoFormJs](myStates))
    )
}
