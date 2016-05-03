package com.all4journey.webapp.util

import com.wix.accord._
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $}
import com.wix.accord.{Success => ValidationSuccess}
import prickle.Unpickle

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._
import com.all4journey.shared.domain._

// $COVERAGE-OFF$
trait NavPills {
  @JSExport
  def load(active:String) = ul(cls := "nav nav-pills")(
    li(id := "personalInfoLink", role := "presentation", if(active.equalsIgnoreCase("personalInfoLink")) cls := "active" else "")(
      a(href := "/multiformProfile/personal")("Personal Info")
    ),
    li(id := "placesLink", role := "presentation", if(active.equalsIgnoreCase("placesLink")) cls := "active" else "")(
      a(href := "/multiformProfile/places")("Your Places")
    ),
    li(id := "passwordChangeLink", role := "presentation", if(active.equalsIgnoreCase("passwordChangeLink")) cls := "active" else "")(
      a(href := "/multiformProfile/password")("Change Password")
    )
  )
}

object NavPills extends NavPills

trait AddressForm extends AddressTypePickler {

  @JSExport
  val emptyAddress = Address("0", "0", None, State("NONE", "Choose a state"), "", HomeAddressType, "Home")

  @JSExport
  def load(address: Option[Address]) = div(id := "addressFieldsDiv", "addressUuid".attr := address.getOrElse(emptyAddress).id)(

    div(id := "streetAddressDiv", cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Street Address:"),
      div(cls := "col-lg-8")(
        div(id := "streetAddressHelpBlock"),
        input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "text", value := address.getOrElse(emptyAddress).street.getOrElse(""))
      )
    ),
    div(id := "stateDiv", cls := "form-group")(
      label(cls := "col-lg-3 control-label")("State:"),
      div(cls := "col-lg-8")(
        div(id := "stateHelpBlock"),
        div(cls := "ui-select")(
          select(id := "userState", name := "userState", cls := "form-control partOfStateList")(
            option(value := address.getOrElse(emptyAddress).state.id, selected := "selected")(address.getOrElse(emptyAddress).state.description)
          )
        )
      )
    ),
    div(id := "zipCodeDiv", cls := "form-group")(
      label(cls := "col-lg-3 control-label")("Zip code:"),
      div(cls := "col-lg-8")(
        div(id := "zipCodeHelpBlock"),
        input(id := "zipCode", name := "zipCode", cls := "form-control partOfZipCodeList", `type` := "text", value := address.getOrElse(emptyAddress).zipCode)
      )
    )
  )

  def setViolationPrompts(violations: Set[Violation]) = {
    if (!violations.isEmpty) {
      //                      val content = dom.document.getElementById("content")
      //                      for (v <- failureList)
      //                        content.appendChild(p(v.description).render)
      violations.foreach(violation =>
        violation.description.getOrElse("") match {
          case street if street.contains("street") =>
            HtmlHelper.showHelpBlock("#streetAddressDiv", "streetAddressHelpBlock", "invalid street address format")
          case state if state.contains("state") =>
            HtmlHelper.showHelpBlock("#stateDiv", "stateHelpBlock", "please choose a state")
          case zipCode if zipCode.contains("zipCode") =>
            HtmlHelper.showHelpBlock("#zipCodeDiv", "zipCodeHelpBlock", "invalid zip code format")
        }
      )
    }
  }

  def doValidation(address: Address): Set[Violation] = {
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
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && (!address.zipCode.isEmpty))
        validate(address)(Address.validatorNoStreetNoStateWithZip)
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && (address.zipCode.isEmpty))
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

  def buildObjectFromForm(): Address = {
    val addressUuid = $("#addressFieldsDiv").attr("addressUuid").toString.trim
    val streetAddress = $("#streetAddress").value().toString.trim
    val stateId = $("#userState").value().toString.trim
    val zipCode = $("#zipCode").value().toString.trim

    new Address(addressUuid, "0", Option(streetAddress), State(stateId, ""), zipCode, HomeAddressType, "Home")
  }

  def refresh(data: js.Any): Unit = {
    Unpickle[Address].fromString(s"$data") match {
      case Success(someAddress) =>
        refreshFields(someAddress)
      case _ =>
        $("#errorBanner").show()
    }
  }

  def refreshFields(someAddress: Address): Unit = {
    $("#placeName").value(someAddress.placeName)
    $("#streetAddress").value(someAddress.street.getOrElse(""))
    $("#userState").value(someAddress.state.id).change()
    $("#zipCode").value(someAddress.zipCode)
  }

  def refreshUuid(data: js.Any): Unit = {
    Unpickle[PersonalFormData].fromString(s"$data") match {
      case Success(somePersonalFormData) =>
        $("#addressFieldsDiv").attr("addressUuid", somePersonalFormData.address.getOrElse(emptyAddress).id)
        HtmlHelper.showSuccessBanner
      case _ =>
        HtmlHelper.showErrorBanner
    }
  }
}

object AddressForm extends AddressForm
// $COVERAGE-ON$