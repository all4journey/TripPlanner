package com.all4journey.webapp.pages

import com.all4journey.shared.domain._
import com.all4journey.webapp.util.{HtmlHelper, AddressForm, AjaxHelper, NavPills}
import org.scalajs.dom
import prickle._
import org.scalajs.jquery.{jQuery => $}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._
import com.wix.accord.{Success => ValidationSuccess}
import com.wix.accord._

/**
  * Created by aabreu on 1/17/16.
  */
// $COVERAGE-OFF$
object PersonalInfoFormJsImpl extends PersonalInfoFormJs  with AddressTypePickler {

  def run(): Unit = {}

  def runWithParams(params: Any): Unit = {

    val (token, formData) = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success((token:String, fd: PersonalFormData)) => (token, fd)
      case _ => throw new IllegalStateException("the back end didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(personalInfoForm(formData.user, formData.address, token).render)

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
  def personalInfoForm(user: User, homeAddress: Option[Address], token:String) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        NavPills.load("personalInfoLink")
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
            div(id := "firstNameDiv", cls := "form-group")(
              label(cls := "col-lg-3 control-label")("First name:"),
              div(cls := "col-lg-8 controls")(
                div(id := "firstNameHelpBlock"),
                input(id := "firstName", name := "firstName", cls := "form-control", `type` := "text", value := user.fName)
              )
            ),
            div(id := "lastNameDiv", cls := "form-group")(
              label(cls := "col-lg-3 control-label")("Last name:"),
              div(cls := "col-lg-8")(
                div(id := "lastNameHelpBlock"),
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
            AddressForm.load(homeAddress)
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save", onclick := { () =>

                  $(".has-error").removeClass("has-error")
                  $(".help-block").remove()

                  val firstName = $("#firstName").value().toString.trim
                  val lastName =  $("#lastName").value().toString.trim
                  val emailAddress = $("#email").value().toString.trim

                  val user = new User("0", firstName, lastName, emailAddress, "", None)

                  val userValidationResult = validate(user)

                  val validUser = userValidationResult match {
                    case ValidationSuccess => true
                    case Failure(failureList) =>
                      failureList.foreach(violation =>
                        if (violation.description.getOrElse("").contains("fName")) {
                          HtmlHelper.showHelpBlock("#firstNameDiv", "firstNameHelpBlock", "first name must be less than 50 characters")
                        }
                        else if(violation.description.getOrElse("").contains("lName")) {
                          HtmlHelper.showHelpBlock("#lastNameDiv", "lastNameHelpBlock", "last name must be less than 50 characters")
                        }
                      )
                      false
                  }

                  val address = AddressForm.buildObjectFromForm()

                  val addressViolations = Address.doValidation(address)
                  AddressForm.setViolationPrompts(addressViolations)

                  if (validUser && addressViolations.isEmpty) {
                    val personalFormPayload = new PersonalFormData(user, Some(address), Seq[State]())
                    val pickledPfp = Pickle.intoString(personalFormPayload)


                    AjaxHelper.doAjaxPostWithJson("/multiformProfile/personal", pickledPfp, "", AddressForm.refreshUuid, HtmlHelper.showErrorBanner)

                  }
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
}
// $COVERAGE-ON$