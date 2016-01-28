package com.all4journey.webapp.pages

import com.all4journey.shared.domain.{PersonalFormData, State, Address}
import com.all4journey.webapp.util.NavPills
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import prickle.Unpickle
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object PersonalInfoFormJsImpl extends PersonalInfoFormJs with NavPills{
  var formIndex:Int = 1
  var formData: PersonalFormData = null

  def run(params: PersonalFormData): Unit = {}

  def runWithParams(params: Any): Unit = {

    formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(fd: PersonalFormData) => fd
      case _ => PersonalFormData(None, Seq[Address](), Seq[State]())
    }

    val content = dom.document.getElementById("content")
    content.appendChild(personalInfoForm.render)

    if (formData.addresses.isEmpty) {
      addAddressPanelWithAddButton(None)
    }
    else {
      val numberOfAddresses = formData.addresses.size - 1
      for (x <- 0 until numberOfAddresses) {
        addAddressPanelWithMinusButton(formData.addresses(x))
      }

      addAddressPanelWithAddButton(Some(formData.addresses.last))
    }

    $(".btn-add").click(addMoreAddressFields _)
  }

  @JSExport
  def addAddressPanelWithAddButton(address: Option[Address]): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(addressTextFields("btn btn-success btn-add", "glyphicon glyphicon-plus", "has-success", 0).render)

    buildStatesDropDown(0)

    //formIndex += 1
  }

  @JSExport
  def addAddressPanelWithMinusButton(address: Address): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(addressTextFields("btn btn-danger btn-sub", "glyphicon glyphicon-minus", "has-error", formIndex).render)

    buildStatesDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def buildStatesDropDown(dropDownIndex: Int): Unit = {

    val stateDropdown = dom.document.getElementById("userState" + dropDownIndex)

    for (stateItem <- formData.states) {
      val option = dom.document.createElement("option")
      option.textContent = stateItem.description
      option.setAttribute("value", stateItem.id)
      stateDropdown.appendChild(option)
    }
  }

  @JSExport
  def addMoreAddressFields(): Unit = {
    $("#streetAddressDiv0").before(addressTextFields("btn btn-danger btn-sub", "glyphicon glyphicon-minus", "has-error", formIndex).render)
    $("#plusMinusButton" + formIndex).click({ (hTMLElement: HTMLElement) =>
      val index = $(hTMLElement).attr("index")
      $("#addressFieldsDiv" + index).remove()
    } : js.ThisFunction)

    buildStatesDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def addressTextFields(buttonClass: String, glyphClass: String,  highlightClass:String, formIndex: Int) = div(id := "addressFieldsDiv" + formIndex)(
    div(id := "streetAddressDiv" + formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("Street Address:"),
    div(cls := "col-lg-8")(
      div(cls := "input-group")(
        input(id := "streetAddress" + formIndex, name := "streetAddress" + formIndex, cls := "form-control", `type` := "text"),
        span(cls := "input-group-btn")(
          button(id := "plusMinusButton" + formIndex, cls := buttonClass, `type` := "button", onmouseover := { () =>
            $("#streetAddressDiv" + formIndex).addClass(highlightClass)
            $("#stateDiv" + formIndex).addClass(highlightClass)
            $("#zipCodeDiv" + formIndex).addClass(highlightClass)
          }, onmouseout := { () =>
            $("#streetAddressDiv" + formIndex).removeClass(highlightClass)
            $("#stateDiv" + formIndex).removeClass(highlightClass)
            $("#zipCodeDiv" + formIndex).removeClass(highlightClass)
          }, "index".attr := formIndex)(
          span(cls := glyphClass, style := "font-size:1.45em;"))
        )
      )
    )
  ),
  div(id := "stateDiv"+ formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("State:"),
    div(cls := "col-lg-8")(
      div(cls := "ui-select")(
        select(id := "userState" + formIndex, name := "userState" + formIndex, cls := "form-control")(
          option(value := "NONE", selected := "selected")("Choose a state")
        )
      )
    )
  ),
  div(id := "zipCodeDiv" + formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("Zip code:"),
    div(cls := "col-lg-8")(
      input(id := "zipCode" + formIndex, name := "zipCode" + formIndex, cls := "form-control", `type` := "text")
    )
  )
  )

  @JSExport
  def personalInfoForm = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        getNavPills("personalInfoLink")
      )
    ),
    h1(cls := "page-header"),
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
          div(id := "main-form-group")(
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
            )
          ),
          div(
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