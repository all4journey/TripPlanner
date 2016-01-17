package com.tripPlanner.webapp.pages

import org.scalajs.dom
import prickle.Unpickle
import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

// $COVERAGE-OFF$
object AjaxPasswordChangeFormJsImpl extends AjaxPasswordChangeFormJs {
  def run(params: String): Unit = {}

  def runWithParams(params: Any): Unit = {

    val emailAddress = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(ea: String) => ea
      case _ => ""
    }

    val content = dom.document.getElementById("ajaxContent")
    content.appendChild(passwordChangeForm(emailAddress).render)
  }

  def passwordChangeForm(emailAddress: String) = div(
    div(cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Email:"),
      div(cls := "col-lg-8")(
        input(id := "email", name := "email", cls := "form-control disabled", `type` := "text", value := emailAddress, disabled)
      )
    ),
    div(cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Current Password:"),
      div(cls := "col-lg-8")(
        input(id := "currentPassword", name := "currentPassword", cls := "form-control", `type` := "password")
      )
    ),
    div(cls := "form-group")(
      label(cls := "col-lg-3 control-label")("New Password:"),
      div(cls := "col-lg-8")(
        input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "password")
      )
    ),
    div(cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Re-enter New Password:"),
      div(cls := "col-lg-8")(
        input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "password")
      )
    )
  )
}
// $COVERAGE-ON$
