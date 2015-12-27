package com.tripPlanner.webapp.pages

import scala.scalajs.js.Dynamic.global

/**
  * Created by rjkj on 12/5/15.
  */
object IndexJsImpl extends IndexJs{
  def run(): Unit = {
    global.document.getElementById("content").textContent = "More content to come!"
  }
}


