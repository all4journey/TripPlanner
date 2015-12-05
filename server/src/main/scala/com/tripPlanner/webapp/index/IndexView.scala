package com.tripPlanner.webapp.index

import com.tripPlanner.webapp.{MainTemplate, View}
import scalatags.Text.all._

/**
  * Created by rjkj on 12/5/15.
  */
trait IndexView extends View {
  def apply() =
    MainTemplate(
      titleText = "It Works",
      content = Seq(
        p("Hello World")
      ),
      footer = Seq(jsModule[IndexJs])
    )
}

object IndexView extends IndexView