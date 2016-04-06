package com.all4journey.webapp.pages

import com.all4journey.shared.domain.{PersonalFormData, State, Address, User}
import com.all4journey.webapp.util.{AjaxHelper, NavPills}
import org.scalajs.dom
import prickle.{Pickle, Unpickle}
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
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
object PersonalInfoFormJsImpl extends PersonalInfoFormJs with NavPills{

  @JSExport
  val emptyAddress = Address("0", "0", None, State("NONE", "Choose a state"), "", "HOME", "Home")

  def run(): Unit = {}

  def runWithParams(params: Any): Unit = {

    val (token, formData) = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success((token:String, fd: PersonalFormData)) => (token, fd)
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
                input(id := "email", name := "email", cls := "form-control", `type` := "text", value := user.emailAddress, disabled)
              )
            ),
            h3("Home Address"),
            div(id := "homeAddressFieldsDiv", "addressUuid".attr := homeAddress.getOrElse(emptyAddress).id)(

              div(id := "streetAddressDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Street Address:"),
                div(cls := "col-lg-8")(
                  div(id := "streetAddressHelpBlock"),
                  input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "text", value := homeAddress.getOrElse(emptyAddress).street.getOrElse(""))
                )
              ),
              div(id := "stateDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("State:"),
                div(cls := "col-lg-8")(
                  div(id := "stateHelpBlock"),
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
                  div(id := "zipCodeHelpBlock"),
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
                          showHelpBlock("#firstNameDiv", "firstNameHelpBlock", "first name must be less than 50 characters")
                        }
                        else if(violation.description.getOrElse("").contains("lName")) {
                          showHelpBlock("#lastNameDiv", "lastNameHelpBlock", "last name must be less than 50 characters")
                        }
                      )
                      false
                  }

                  val addressUuid = $("#homeAddressFieldsDiv").attr("addressUuid").toString.trim
                  val streetAddress = $("#streetAddress").value().toString.trim
                  val stateId = $("#userState").value().toString.trim
                  val zipCode = $("#zipCode").value().toString.trim

                  val address = new Address(addressUuid, "0", Option(streetAddress), State(stateId, ""), zipCode, "HOME", "Home")

                  val addressViolations = validateAddress(address)
                  setUiViolationPrompts(addressViolations)

                  if (validUser && addressViolations.isEmpty) {
                    val personalFormPayload = new PersonalFormData(user, Some(address), Seq[State]())
                    val pickledPfp = Pickle.intoString(personalFormPayload)

                    AjaxHelper.doAjaxPostWithJson("/multiformProfile/personal", pickledPfp, refreshForm, showErrorBanner)
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

  private def setUiViolationPrompts(violations: Set[Violation]) = {
    if (!violations.isEmpty) {
      //                      val content = dom.document.getElementById("content")
      //                      for (v <- failureList)
      //                        content.appendChild(p(v.description).render)
      violations.foreach(violation =>
        if (violation.description.getOrElse("").contains("street")) {
          $("#streetAddressDiv").addClass("has-error")
          val streetAddressHelpBlock = dom.document.getElementById("streetAddressHelpBlock")
          streetAddressHelpBlock.appendChild(span(cls := "help-block")("invalid street address format").render)
        }
        else if (violation.description.getOrElse("").contains("state")) {
          showHelpBlock("#stateDiv", "stateHelpBlock", "please choose a state")
        }
        else if (violation.description.getOrElse("").contains("zipCode")) {
          showHelpBlock("#zipCodeDiv", "zipCodeHelpBlock", "invalid zip code format")

        }
      )
    }
  }

  private def validateAddress(address: Address): Set[Violation] = {
    var violations = Set.empty[Violation]
    val isFullAddressValid = validate(address)(Address.validatorWithStreet) match {
      case ValidationSuccess   => true
      case Failure(failureSet) =>
        violations = failureSet
        false
    }

    if (!isFullAddressValid) {
      val partialAddressValidationResult = if (address.street.getOrElse("").isEmpty && !address.state.id.equals("NONE"))
        validate(address)(Address.validatorNoStreetWithState)
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && (address.zipCode != null && !address.zipCode.isEmpty))
        validate(address)(Address.validatorNoStreetNoStateWithZip)
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && (address.zipCode == null || address.zipCode.isEmpty))
      // an empty address is a valid address
        ValidationSuccess

      violations = partialAddressValidationResult match {
        case ValidationSuccess   => Set.empty[Violation]
        case Failure(failureSet) => failureSet
        case _ => violations
      }
    }

    violations
  }

  private def showHelpBlock(divName: String, helpBlockName: String, helpBlockMessage: String): Unit = {
    val containsErrorClass = $(divName).hasClass("has-error")
    if (!containsErrorClass) {
      $(divName).addClass("has-error")
      val helpBlock = dom.document.getElementById(helpBlockName)
      helpBlock.appendChild(span(cls := "help-block")(helpBlockMessage).render)
    }
  }

  private def refreshForm(data: js.Any): Unit = {
    Unpickle[PersonalFormData].fromString(s"$data") match {
      case Success(somePersonalFormData) =>
        $("#homeAddressFieldsDiv").attr("addressUuid", somePersonalFormData.address.getOrElse(emptyAddress).id)
        showSuccessBanner
      case _ =>
        showErrorBanner
    }
  }

  private def showErrorBanner(): Unit = {
    $("#errorBanner").show()
    $("#successBanner").hide()
  }

  private def showSuccessBanner(): Unit = {
    $("#successBanner").show()
    $("#errorBanner").hide()
  }
}
// $COVERAGE-ON$