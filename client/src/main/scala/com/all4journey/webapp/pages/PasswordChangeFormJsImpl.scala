package com.all4journey.webapp.pages

import com.all4journey.webapp.util.NavPills
import org.scalajs.dom
import prickle.Unpickle

import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object PasswordChangeFormJsImpl extends PasswordChangeFormJs with NavPills{
  def run(params: String): Unit = {}

  def runWithParams(params: Any): Unit = {

    val emailAddress = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(ea: String) => ea
      case _ => ""
    }

    val content = dom.document.getElementById("content")
    content.appendChild(passwordChangeForm(emailAddress).render)
  }

  def passwordChangeForm(emailAddress: String) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        getNavPills("passwordChangeLink")
      )
    ),
    h1(cls := "page-header"),
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
          div(
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
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-5 control-label")(),
            div(cls := "col-md-7")(
              input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save Changes"),
              span(),
              input(id := "cancelButton", `type` := "reset", cls := "btn btn-default", value := "Cancel")
            )
          )
        )
      )
    )

  )
}
// $COVERAGE-ON$
