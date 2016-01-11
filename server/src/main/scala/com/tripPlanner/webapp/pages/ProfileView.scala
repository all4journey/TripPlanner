package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{MainTemplate, View}
import com.tripPlanner.shared.domain.State


/**
  * Created by aabreu on 12/6/15.
  */
class ProfileView(states: Seq[State]) extends View {
  val myStates = states
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Edit Profile",
      footer = Seq(jsModuleWithParams[ProfileJs](myStates))
    )
}

trait AjaxProfileView extends View {
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Profile",
      footer = Seq(jsModule[AjaxProfileJs])
    )
}

object AjaxProfileView extends AjaxProfileView