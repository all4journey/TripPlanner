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
class ProfileJsImpl extends ProfileJs {
  def run(): Unit = {
    val content = dom.document.getElementById("content")
    content.appendChild(profilePanel.render)

  }

  def alertBanner = div()

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
        alertBanner,
        h3("Personal Info"),
        form(cls := "form-horizontal", role := "form", action := "profile", method := "post")(
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
            label(cls := "col-lg-3 control-label")("Company:"),
            div(cls := "col-lg-8")(
              input(id := "company", name := "company", cls := "form-control", `type` := "text")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Time Zone:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "userTimezone", name := "userTimezone", cls := "form-control")(
                  option(value := "Hawaii")("(GMT-10:00) Hawaii"),
                  option(value := "Alaska")("(GMT-09:00) Alaska"),
                  option(value := "Pacific Time (US & Canada)")("(GMT-08:00) Pacific Time (US & Canada)"),
                  option(value := "Arizona")("(GMT-07:00) Arizona"),
                  option(value := "Mountain Time (US & Canada)")("(GMT-07:00) Mountain Time (US & Canada)"),
                  option(value := "Central Time (US & Canada)")("(GMT-06:00) Central Time (US & Canada)"),
                  option(value := "Eastern Time (US & Canada)", selected := "selected")("(GMT-05:00) Eastern Time (US & Canada)"),
                  option(value := "Indiana (East)")("(GMT-05:00) Indiana (East)")
                )
              )
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
                  option(value := "NY", selected := "selected")("New York"),
                  option(value := "PA")("Pennsylvania"),
                  option(value := "GA")("Georgia")
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
              input(id := "saveButton", `type` := "submit", cls := "btn btn-primary", value := "Save Changes"),
              span(),
              input(id := "cancelButton", `type` := "reset", cls := "btn btn-default", value := "Cancel")
            )
          )
        )
      )
    )
  )

}

object ProfileJsImpl extends ProfileJsImpl