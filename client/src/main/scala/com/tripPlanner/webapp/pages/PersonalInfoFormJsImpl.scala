package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{PersonalFormData, State, Address}
import org.scalajs.dom
import prickle.Unpickle

import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object PersonalInfoFormJsImpl extends PersonalInfoFormJs {
  def run(params: PersonalFormData): Unit = {}

  def runWithParams(params: Any): Unit = {

    val formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(fd: PersonalFormData) => fd
      case _ => PersonalFormData(None, Seq[Address](), Seq[State]())
    }

    val content = dom.document.getElementById("content")

    if (formData.addresses.isEmpty) {
      content.appendChild(personalInfoForm(Address("", "", None, State("NONE", "Choose a state"), ""), 1).render)
    }
    else {
      var formIndex = 1
      for (addressItem <- formData.addresses) {
        content.appendChild(personalInfoForm(addressItem, formIndex).render)
        formIndex += 1
      }
    }

    val stateDropdown = dom.document.getElementById("userState")

    for (stateItem <- formData.states) {
      val option = dom.document.createElement("option")
      option.textContent = stateItem.description
      option.setAttribute("value", stateItem.id)
      stateDropdown.appendChild(option)
    }

  }

  def personalInfoForm(address: Address, formIndex: Int) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        ul(cls := "nav nav-pills")(
          li(id := "personalInfoLink", role := "presentation", cls := "active")(
            a(href := "/multiformProfile/personal")("Personal Info")
          ),
          li(id := "vehicleInfoLink", role := "presentation")(
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
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("First name:"),
              div(cls := "col-lg-8")(
                input(id := "firstName", name := "firstName", cls := "form-control", `type` := "text")
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Last name:"),
              div(cls := "col-lg-8")(
                input(id := "lastName", name := "lastName", cls := "form-control", `type` := "text")
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Street Address:"),
              div(cls := "col-lg-8")(
                input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "text")
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("State:"),
              div(cls := "col-lg-8")(
                div(cls := "ui-select")(
                  select(id := "userState", name := "userState", cls := "form-control")(
                    option(value := "NONE", selected := "selected")("Choose a state")
                  )
                )
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Zip code:"),
              div(cls := "col-lg-8")(
                input(id := "zipCode", name := "zipCode", cls := "form-control", `type` := "text")
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

  )
}
// $COVERAGE-ON$