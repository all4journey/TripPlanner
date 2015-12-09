package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{MainTemplate, View}


/**
  * Created by aabreu on 12/6/15.
  */
trait ProfileView extends View {
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Edit Profile",
      footer = Seq(jsModule[ProfileJs])
    )
}

object ProfileView extends ProfileView