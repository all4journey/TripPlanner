package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{User, PersonalFormData, State, Address}
import com.tripPlanner.webapp.util.{AjaxHelper, NavPills}
import org.scalajs.dom
import prickle.{Pickle, Unpickle}
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

  @JSExport
  val emptyAddress = Address("0", "0", None, State("NONE", "Choose a state"), "", "HOME", "Home")

  def run(params: PersonalFormData): Unit = {}

  def runWithParams(params: Any): Unit = {

    val formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(successPersonalFormData: PersonalFormData) => successPersonalFormData
      case _ => throw new IllegalStateException("the backend didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(personalInfoForm(formData.user, formData.address).render)

    buildStatesDropDown(formData.states)

    $("#successBanner").hide()
    $("#errorBanner").hide()
  }

  @JSExport
  def buildStatesDropDown(states: Seq[State]): Unit = {

    val stateDropdown = dom.document.getElementById("userState")

    for (stateItem <- states) {
      val option = dom.document.createElement("option")
      option.textContent = stateItem.description
      option.setAttribute("value", stateItem.id)
      stateDropdown.appendChild(option)
    }
  }

  @JSExport
  def personalInfoForm(user: User, homeAddress: Option[Address]) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        getNavPills("personalInfoLink")
      )
    ),
    h1(cls := "page-header"),
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
    div(cls := "row")(
      div(cls := "col-md-10 col-md-offset-1 personal-info")(
        form(cls := "form-horizontal", role := "form")(
          h3("Personal Info"),
          div(id := "main-form-group")(
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("First name:"),
              div(cls := "col-lg-8")(
                input(id := "firstName", name := "firstName", cls := "form-control", `type` := "text", value := user.fName)
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Last name:"),
              div(cls := "col-lg-8")(
                input(id := "lastName", name := "lastName", cls := "form-control", `type` := "text", value := user.lName)
              )
            ),
            div(cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Email address:"),
              div(cls := "col-lg-8")(
                input(id := "email", name := "email", cls := "form-control", `type` := "text", value := user.email, disabled)
              )
            ),
            h3("Home Address"),
            div(id := "homeAddressFieldsDiv", "addressUuid".attr := homeAddress.getOrElse(emptyAddress).id)(

              div(id := "streetAddressDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Street Address:"),
                div(cls := "col-lg-8")(
                  input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "text", value := homeAddress.getOrElse(emptyAddress).street.getOrElse(""))
                )
              ),
              div(id := "stateDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("State:"),
                div(cls := "col-lg-8")(
                  div(cls := "ui-select")(
                    select(id := "userState", name := "userState", cls := "form-control partOfStateList")(
                      option(value := homeAddress.getOrElse(emptyAddress).state.id, selected := "selected")(homeAddress.getOrElse(emptyAddress).state.description)
                    )
                  )
                )
              ),
              div(id := "zipCodeDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Zip code:"),
                div(cls := "col-lg-8")(
                  input(id := "zipCode", name := "zipCode", cls := "form-control partOfZipCodeList", `type` := "text", value := homeAddress.getOrElse(emptyAddress).zipCode)
                )
              )
            )
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save", onclick := { () =>
                  val firstName = $("#firstName").value().toString.trim
                  val lastName =  $("#lastName").value().toString.trim
                  val emailAddress = $("#email").value().toString.trim

                  val user = new User("0", firstName, lastName, emailAddress, None)

                  val addressUuid = $("#homeAddressFieldsDiv").attr("addressUuid").toString.trim
                  val streetAddress = $("#streetAddress").value().toString.trim
                  val stateId = $("#userState").value().toString.trim
                  val zipCode = $("#zipCode").value().toString.trim

                  val address = new Address(addressUuid, "0", Some(streetAddress), State(stateId, ""), zipCode, "HOME", "Home")

                  val personalFormPayload = new PersonalFormData(user, Some(address), Seq[State]())
                  val pickledPfp = Pickle.intoString(personalFormPayload)

                  AjaxHelper.doAjaxPostWithJson("/multiformProfile/personal", pickledPfp, refreshForm, showErrorBanner)

                }),
                span(),
                input(id := "cancelButton", `type` := "reset", cls := "btn btn-default", value := "Cancel", onclick := { () =>
                  dom.window.location.reload(true);
                })
              )
            )
          )

        )
      )
    )

  )

  private def refreshForm(data: js.Any): Unit = {
    Unpickle[PersonalFormData].fromString(s"$data") match {
      case Success(somePersonalFormData) =>
        $("#homeAddressFieldsDiv").attr("addressUuid", somePersonalFormData.address.getOrElse(emptyAddress).id)
      case _ =>
        $("#errorBanner").show()
    }

    $("#successBanner").show()
  }

  private def showErrorBanner(): Unit = {
    $("#errorBanner").show()
  }
}
// $COVERAGE-ON$