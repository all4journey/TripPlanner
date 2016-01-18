package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.Vehicle
import org.scalajs.dom
import prickle.Unpickle

import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object VehicleInfoFormJsImpl extends VehicleInfoFormJs {
  def run(params: Seq[Vehicle]): Unit = {}

  def runWithParams(params: Any): Unit = {

    val vehicles = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(vs: Seq[Vehicle]) => vs
      case _ => Seq[Vehicle]()
    }

    val content = dom.document.getElementById("content")

    if (vehicles.isEmpty) {
      content.appendChild(vehicleForm(Vehicle("", "", None, None, None), 1).render)
    } else {
      var formIndex = 1
      for (vehicleItem <- vehicles) {
        content.appendChild(vehicleForm(vehicleItem, formIndex).render)
        formIndex += 1
      }
    }
    val vehicleYearDropdown = dom.document.getElementById("userVehicleYear")
    for(yearInLoop <- 2016 to 1950 by -1) {
      val option = dom.document.createElement("option")
      option.textContent = yearInLoop + ""
      option.setAttribute("value", yearInLoop + "")
      vehicleYearDropdown.appendChild(option)
    }
  }

  def vehicleForm(vehicle: Vehicle, formIndex: Int) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        ul(cls := "nav nav-pills")(
          li(id := "personalInfoLink", role := "presentation")(
            a(href := "/multiformProfile/personal")("Personal Info")
          ),
          li(id := "vehicleInfoLink", role := "presentation", cls := "active")(
            a(href := "/multiformProfile/vehicle")("Vehicle Info")
          ),
          li(id := "passwordChangeLink", role := "presentation")(
            a(href := "/multiformProfile/password")("Change Password")
          )
        )
      )
    ),
    h1(cls := "page-header"),
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
          div(div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Year:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "userVehicleYear", name := "userVehicleYear", cls := "form-control")(
                  option(value := "NONE", selected := "selected")("Choose your vehicle year")
                )
              )
            )
          ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Make:"),
              div(cls := "col-lg-8")(
                input(id := "make", name := "make", cls := "form-control", `type` := "text")
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Model:"),
              div(cls := "col-lg-8")(
                input(id := "model", name := "model", cls := "form-control", `type` := "text")
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