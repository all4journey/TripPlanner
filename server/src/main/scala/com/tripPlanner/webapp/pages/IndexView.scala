package com.tripPlanner.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.tripPlanner.webapp.{MainTemplate, View}

import scalatags.Text.all._

/**
  * Created by rjkj on 12/5/15.
  */
trait IndexView extends View {
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Welcome to Trip Planner",
      footer = Seq(jsModule[IndexJs])
    )
}

object IndexView extends IndexView