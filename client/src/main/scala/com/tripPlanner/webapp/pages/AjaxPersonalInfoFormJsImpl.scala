package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.State
import com.tripPlanner.webapp.pages.AjaxPasswordChangeFormJsImpl._
import org.scalajs.dom
import prickle.Unpickle

import scala.scalajs.js
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/10/16.
  */
object AjaxPersonalInfoFormJsImpl extends AjaxPersonalInfoFormJs {

  def run(params: Seq[State]): Unit = {}

  def runWithParams(params: Any): Unit = {

    val content = dom.document.getElementById("ajaxContent")
    content.appendChild(personalInfoForm.render)

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

  def personalInfoForm = div(
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
    )

  )

}
