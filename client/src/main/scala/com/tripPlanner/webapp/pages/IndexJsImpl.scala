package com.tripPlanner.webapp.pages

import scala.scalajs.js.Dynamic.global
import com.tripPlanner.webapp.logger.log

/**
  * Created by rjkj on 12/5/15.
  */
object IndexJsImpl extends IndexJs{
  def run(): Unit = {
    log.info("it works")
    global.document.getElementById("content").textContent = "More content to come!"
  }
}


