package com.tripPlanner.webapp.pages

import com.tripPlanner.webapp.pages.ProfileJs

import scala.scalajs.js
import js.Dynamic.{ global => g }
import org.scalajs.dom
import scalatags.JsDom.all._
import org.scalajs.jquery.{ jQuery => $ }



/**
  * Created by rjkj on 12/5/15.
  */
object ProfileSavedJsImpl extends ProfileJsImpl {
  override def alertBanner = div(cls := "alert alert-info alert-dismissable")(
    a(cls := "panel-close close", "data-dismiss".attr := "alert")("×"),
    i(cls := "fa fa-coffee")(),
    strong("Success"), " profile was saved successfully"
  )
}