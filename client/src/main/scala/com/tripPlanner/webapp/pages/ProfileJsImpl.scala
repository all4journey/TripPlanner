package com.tripPlanner.webapp.pages

import scala.scalajs.js
import js.Dynamic.{ global => g }
import org.scalajs.dom
import scalatags.JsDom.all._
import org.scalajs.jquery.{ jQuery => $ }



/**
  * Created by rjkj on 12/5/15.
  */
object ProfileJsImpl extends ProfileJs {
  def run(): Unit = {
    val content = dom.document.getElementById("content")
    content.appendChild(profilePanel.render)

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
        div(cls := "alert alert-info alert-dismissable")(
          a(cls := "panel-close close", "data-dismiss".attr := "alert")("×"),
          i(cls := "fa fa-coffee")(),
          "This is an ", strong(".alert"), ". Use this to show important messages to the user."
        ),
        h3("Personal Info"),
        form(cls := "form-horizontal", role := "form")(
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("First name:"),
            div(cls := "col-lg-8")(
              input(cls := "form-control", `type` := "text", value := "Andy")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Last name:"),
            div(cls := "col-lg-8")(
              input(cls := "form-control", `type` := "text", value := "123")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Company:"),
            div(cls := "col-lg-8")(
              input(cls := "form-control", `type` := "text", value := "")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Time Zone:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "user_time_zone", cls := "form-control")(
                  option(value := "Hawaii")("(GMT-10:00) Hawaii"),
                  option(value := "Alaska")("(GMT-09:00) Alaska"),
                  option(value := "Pacific Time (US &amp; Canada)")("(GMT-08:00) Pacific Time (US &amp; Canada)"),
                  option(value := "Arizona")("(GMT-07:00) Arizona"),
                  option(value := "Mountain Time (US &amp; Canada)")("(GMT-07:00) Mountain Time (US &amp; Canada)"),
                  option(value := "Central Time (US &amp; Canada)")("(GMT-06:00) Central Time (US &amp; Canada)"),
                  option(value := "Eastern Time (US &amp; Canada)", selected := "selected")("(GMT-05:00) Eastern Time (US &amp; Canada)"),
                  option(value := "Indiana (East)")("(GMT-05:00) Indiana (East)")
                )
              )
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Street Address:"),
            div(cls := "col-lg-8")(
              input(cls := "form-control", `type` := "text", value := "2850 Some Street")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("State:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "userState", cls := "form-control")(
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
              input(cls := "form-control", `type` := "text", value := "")
            )
          ),
          h3("Vehicle Info"),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Year:"),
            div(cls := "col-lg-8")(
              div(cls := "ui-select")(
                select(id := "userVehicleYear", cls := "form-control")(
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
              input(cls := "form-control", `type` := "text")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")("Model:"),
            div(cls := "col-lg-8")(
              input(cls := "form-control", `type` := "text")
            )
          ),
          div(cls := "form-group")(
            label(cls := "col-lg-3 control-label")(),
            div(cls := "col-md-8")(
              input(`type` := "button", cls := "btn btn-primary", value := "Save Changes"),
              span(),
              input(`type` := "reset", cls := "btn btn-default", value := "Cancel")
            )
          )
        )
      )
    )
  )

}