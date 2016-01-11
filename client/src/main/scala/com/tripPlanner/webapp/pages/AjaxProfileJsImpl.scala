package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain._
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import prickle.{Pickle, Unpickle}
import scala.scalajs.js
import scalatags.JsDom.all._
import com.tripPlanner.webapp.util.AjaxHelper

// $COVERAGE-OFF$
/**
  * Created by aabreu on 1/10/16.
  */
object AjaxProfileJsImpl extends AjaxProfileJs {
  def run(): Unit = {
    val content = dom.document.getElementById("content")
    content.appendChild(profileNavBar.render)

    loadPersonalInfoForm()

  }

  def profileNavBar = div(cls := "container")(
    div(cls := "row-fluid")(
        div(cls := "col-sm-12 col-sm-offset-4")(
          ul(cls := "nav nav-pills")(
            li(id := "personalInfoLink", role := "presentation", cls := "active")(
              a(href := "#", onclick := { () =>
                $("#currentForm").empty()
                $("#personalInfoLink").attr("class", "active")
                $("#vehicleInfoLink").removeAttr("class")
                $("#passwordChangeLink").removeAttr("class")
                loadPersonalInfoForm
              })("Personal Info")
            ),
            li(id := "vehicleInfoLink", role := "presentation")(
              a(href := "#", onclick := { () =>
                $("#currentForm").empty()
                $("#personalInfoLink").removeAttr("class")
                $("#vehicleInfoLink").attr("class", "active")
                $("#passwordChangeLink").removeAttr("class")
                loadVehicleForm
              })("Vehicle Info")
            ),
            li(id := "passwordChangeLink", role := "presentation")(
              a(href := "#", onclick := { () =>
                $("#currentForm").empty()
                $("#personalInfoLink").removeAttr("class")
                $("#vehicleInfoLink").removeAttr("class")
                $("#passwordChangeLink").attr("class", "active")
                loadPasswordChangeForm
              })("Change Password")
            )
          )
        )
    ),
    h1(cls := "page-header"),
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
        div(id := "currentForm"),
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

  private def loadPersonalInfoForm(): Unit = {
    AjaxHelper.doAjaxCall("ajaxProfile/personal", addToFormContent, handleAjaxError)
  }

  private def loadPasswordChangeForm(): Unit = {
    AjaxHelper.doAjaxCall("ajaxProfile/passwordMgr", addToFormContent, handleAjaxError)
  }

  private def loadVehicleForm(): Unit = {
    AjaxHelper.doAjaxCall("ajaxProfile/vehicle", addToFormContent, handleAjaxError)
  }

  private def addToFormContent(data: js.Any): Unit = {
    $("#currentForm").append(data)
  }

  private def handleAjaxError(): Unit = {
    println("hello world")
  }
}
// $COVERAGE-ON$