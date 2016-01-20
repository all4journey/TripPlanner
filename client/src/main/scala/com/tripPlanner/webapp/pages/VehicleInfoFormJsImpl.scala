package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.Vehicle
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.jquery.{jQuery => $, _}
import prickle.Unpickle

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object VehicleInfoFormJsImpl extends VehicleInfoFormJs {

  var formIndex = 1
  var vehicles: Seq[Vehicle] = null

  def run(params: Seq[Vehicle]): Unit = {}

  def runWithParams(params: Any): Unit = {

    vehicles = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(vs: Seq[Vehicle]) => vs
      case _ => Seq[Vehicle]()
    }

    val content = dom.document.getElementById("content")
    content.appendChild(vehicleForm.render)

    if (vehicles.isEmpty) {
      addVehiclePanelWithAddButton(None)
    } else {
      val numberOfVehicles = vehicles.size - 1
      for (x <- 0 until numberOfVehicles) {
        addVehiclePanelWithMinusButton(vehicles(x))
      }

      addVehiclePanelWithAddButton(Some(vehicles.last))
    }

    $(".btn-add").click(addMoreVehicleFields _)
  }

  @JSExport
  def addMoreVehicleFields: Unit = {
    $("#yearDiv0").before(vehicleTextFields("btn btn-danger btn-sub", "glyphicon glyphicon-minus", "has-error", formIndex).render)
    $("#plusMinusButton" + formIndex).click({ (hTMLElement: HTMLElement) =>
      val index = $(hTMLElement).attr("index")
      $("#vehicleFieldsDiv" + index).remove()
    } : js.ThisFunction)

    buildVehicleYearDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def buildVehicleYearDropDown(dropDownIndex: Int): Unit = {
    val vehicleYearDropdown = dom.document.getElementById("userVehicleYear" + dropDownIndex)
    for(yearInLoop <- 2016 to 1950 by -1) {
      val option = dom.document.createElement("option")
      option.textContent = yearInLoop + ""
      option.setAttribute("value", yearInLoop + "")
      vehicleYearDropdown.appendChild(option)
    }
  }

  @JSExport
  def addVehiclePanelWithMinusButton(vehicle: Vehicle): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(vehicleTextFields("btn btn-danger btn-sub", "glyphicon glyphicon-minus", "has-error", formIndex).render)

    buildVehicleYearDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def addVehiclePanelWithAddButton(vehicle: Option[Vehicle]): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(vehicleTextFields("btn btn-success btn-add", "glyphicon glyphicon-plus", "has-success", 0).render)

    buildVehicleYearDropDown(0)

    //formIndex += 1
  }

  @JSExport
  def vehicleTextFields(buttonClass: String, glyphClass: String,  highlightClass:String, formIndex: Int) = div(id := "vehicleFieldsDiv" + formIndex)(
    div(id := "yearDiv" + formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("Year:"),
    div(cls := "col-lg-8")(
      div(cls := "input-group")(
      div(cls := "ui-select")(
        select(id := "userVehicleYear" + formIndex, name := "userVehicleYear" + formIndex, cls := "form-control")(
          option(value := "NONE", selected := "selected")("Choose your vehicle year")
        )
      ),
        span(cls := "input-group-btn")(
          button(id := "plusMinusButton" + formIndex, cls := buttonClass, `type` := "button", onmouseover := { () =>
            $("#yearDiv" + formIndex).addClass(highlightClass)
            $("#makeDiv" + formIndex).addClass(highlightClass)
            $("#modelDiv" + formIndex).addClass(highlightClass)
          }, onmouseout := { () =>
            $("#yearDiv" + formIndex).removeClass(highlightClass)
            $("#makeDiv" + formIndex).removeClass(highlightClass)
            $("#modelDiv" + formIndex).removeClass(highlightClass)
          }, "index".attr := formIndex)(
            span(cls := glyphClass, style := "font-size:1.45em;"))
        )
      )
    )
  ),
    div(id := "makeDiv" + formIndex, cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Make:"),
      div(cls := "col-lg-8")(
        input(id := "make" + formIndex, name := "make" + formIndex, cls := "form-control", `type` := "text")
      )
    ),
    div(id := "modelDiv" + formIndex, cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Model:"),
      div(cls := "col-lg-8")(
        input(id := "model" + formIndex, name := "model" + formIndex, cls := "form-control", `type` := "text")
      )
    )
  )

  @JSExport
  def getNavPills(someString:String) = ul(cls := "nav nav-pills")(
    li(id := "personalInfoLink", role := "presentation", if(someString.equalsIgnoreCase("personalInfoLink")) cls := "active" else "")(
      a(href := "/multiformProfile/personal")("Personal Info")
    ),
    li(id := "vehicleInfoLink", role := "presentation", if(someString.equalsIgnoreCase("vehicleinfolink")) cls := "active" else "")(
      a(href := "/multiformProfile/vehicle")("Vehicle Info")
    ),
    li(id := "passwordChangeLink", role := "presentation", if(someString.equalsIgnoreCase("passwordChangeLink")) cls := "active" else "")(
      a(href := "/multiformProfile/password")("Change Password")
    )
  )

  @JSExport
  def vehicleForm = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        getNavPills("vehicleinfolink")
      )
    ),
    h1(cls := "page-header"),
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
          div(id := "main-form-group")(

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