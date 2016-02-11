package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.webapp.{MainTemplate, View}
import com.all4journey.shared.domain.State


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