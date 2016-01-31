package com.all4journey.webapp.pages

import akka.http.scaladsl.server.RequestContext
import com.all4journey.webapp.{MainTemplate, View}

/**
  * Created by rjkj on 1/31/16.
  */
trait LoginView extends View {
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Login",
      footer = Seq(jsModule[LoginJs])
    )
}

object LoginView extends LoginView
