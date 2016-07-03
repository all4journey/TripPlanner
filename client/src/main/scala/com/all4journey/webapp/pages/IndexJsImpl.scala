package com.all4journey.webapp.pages

import org.scalajs.dom
import scalajs.js.Dynamic

// $COVERAGE-OFF$
/**
  * Created by rjkj on 12/5/15.
  */
object IndexJsImpl extends IndexJs{
  def run(): Unit = {
//    val script = dom.document.createElement("script")
//    script.setAttribute("type", "text/javascript")
//    script.innerHTML =
//      "Copper.login({ " +
//          "application_id: '[your application_id]', scope: 'name, phone' " +
//      "}, " +
//      "function(err, user) { " +
//          "console.log('User logged in ->', user); " +
//      "});"

    dom.document.getElementById("content").textContent = "More content to come!"
   // dom.document.head.appendChild(script)
  }
}

// $COVERAGE-ON$


