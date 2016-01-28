package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{User, Profile, Address, State, Vehicle}

import prickle._

import org.scalajs.dom
import scalatags.JsDom.all._
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import scala.scalajs.js
import scala.util.Success

// $COVERAGE-OFF$
/**
  * Created by rjkj on 12/5/15.
  */
@deprecated
object ProfileJsImpl extends ProfileJs {

  def run(params: Seq[State]): Unit = {}

  def runWithParams(params: Any): Unit = {
    val content = dom.document.getElementById("content")
    content.appendChild(profilePanel.render)
    $("#successBanner").hide()
    $("#errorBanner").hide()

    val stateDropdown = dom.document.getElementById("userState")

    Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(states: Seq[State]) =>
        for (stateItem <- states) {
          val option = dom.document.createElement("option")
          option.textContent = stateItem.description
          option.setAttribute("value", stateItem.id)
          stateDropdown.appendChild(option)
        }
      case _ => Seq[State]()
    }
  }

  def profilePanel = div(cls := "container")(
    div(cls := "row")(
      div(cls := "col-md-3")(
        div(cls := "text-center")(
          img(src := "//placehold.it/100", cls := "avatar img-circle", alt := "avatar"),
          h6("Upload a different photo..."),
          input(`type` := "file", cls := "form-control")
        )
      ),
      div(cls := "col-md-9 personal-info")(
        div(id := "successBanner", cls := "alert alert-info alert-dismissable")(
          span(cls := "glyphicon glyphicon-ok", "aria-hidden".attr := "true")(),
          a(cls := "panel-close close", onclick := { () =>
            $("#successBanner").hide();
          })("×"),
          i(cls := "fa fa-coffee")(),
          strong(" Success!"), " profile was saved successfully"
        ),
        div(id := "errorBanner", cls := "alert alert-danger alert-dismissable")(
          span(cls := "glyphicon glyphicon-exclamation-sign", "aria-hidden".attr := "true")(),
          a(cls := "panel-close close", onclick := { () =>
            $("#errorBanner").hide();
          })("×"),
          i(cls := "fa fa-coffee")(),
          strong(" Error!"), " profile was ", strong("not"), " saved successfully"
        ),
        h3("Personal Info"),
        form(cls := "form-horizontal", role := "form")(
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
          div(cls:="form-group")(
            label(cls := "col-lg-3 control-label")("Email address:"),
            div(cls := "col-lg-8")(
              input(id := "emailAddress", name := "emailAddress", cls:= "form-control", `type` :="text")
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
          h3("Vehicle Info"),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Year:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "userVehicleYear", name := "userVehicleYear", cls := "form-control")(
                  option(value := "2011", selected := "selected")("2011"),
                  option(value := "2012")("2012"),
                  option(value := "2013")("2013"),
                  option(value := "2014")("2014"),
                  option(value := "2015")("2015")
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
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")(),
            div(cls := "col-md-8")(
              input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save Changes", onclick := { () =>

                val firstName = $("#firstName").value().toString.trim
                val lastName = $("#lastName").value().toString.trim
                val emailAddress = $("#emailAddress").value.toString.trim
                val user: User = new User(fName = firstName, lName = lastName, emailAddress = emailAddress, registrationDate = None)

                val userStateId = $("#userState :selected").value().toString.trim
                val userStateDescription = $("#userState :selected").text().toString.trim
                val state = new State(userStateId, userStateDescription)

                val streetAddress = $("#streetAddress").value().toString.trim
                val zipCode = $("#zipCode").value().toString.trim
                val address = new Address(street = Option(streetAddress), state = state, zipCode = zipCode)
                val addresses: Seq[Address] = List(address)

                val userVehicleYear = $("#userVehicleYear :selected").value().toString.trim
                val make = $("#make").value().toString.trim
                val model = $("#model").value().toString.trim
                val vehicle = new Vehicle(year = Option(userVehicleYear), make = Option(make), model = Option(model))
                val vehicles: Seq[Vehicle] = List(vehicle)

                val profileInfo = new Profile(user, addresses, vehicles)

                val pickledProfileInfo = Pickle.intoString(profileInfo)

                $.ajax(js.Dynamic.literal(
                  url = "profile",
                  `type` = "post",
                  data = pickledProfileInfo,
                  contentType = "application/json; charset=utf-8",
                  traditional = true,
                  success = { (data: js.Any, jqXHR: JQueryXHR) =>
                    //                     val content = dom.document.getElementById("content")
                    //                     content.appendChild(p(s"$data").render)
                    $("#successBanner").show()
                  },
                  error = { () =>
                    $("#errorBanner").show()
                  }
                ).asInstanceOf[JQueryAjaxSettings])
              }),

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
