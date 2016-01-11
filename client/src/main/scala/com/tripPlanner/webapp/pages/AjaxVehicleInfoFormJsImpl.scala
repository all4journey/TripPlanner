package com.tripPlanner.webapp.pages

import java.util.{Calendar, Date}

import com.tripPlanner.shared.domain.Vehicle
import org.scalajs.dom
import prickle.Unpickle
import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/10/16.
  */
object AjaxVehicleInfoFormJsImpl extends AjaxVehicleInfoFormJs {

  def run(params: Vehicle): Unit = {}

  def runWithParams(params: Any): Unit = {

    val vehicle = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(v: Vehicle) => v
      case _ => Vehicle("", "", None, None, None)
    }

    val content = dom.document.getElementById("ajaxContent")
    content.appendChild(vehicleForm(vehicle).render)

    val vehicleYearDropdown = dom.document.getElementById("userVehicleYear")
    for(yearInLoop <- 2016 to 1950 by -1) {
      val option = dom.document.createElement("option")
      option.textContent = yearInLoop + ""
      option.setAttribute("value", yearInLoop + "")
      vehicleYearDropdown.appendChild(option)
    }
  }

  def vehicleForm(vehicle: Vehicle) = div(div(cls := "form-group")(
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
  )
}
