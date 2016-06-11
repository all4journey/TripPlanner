package com.all4journey.webapp.util

import org.scalajs.jquery.{jQuery => $}
import com.wix.accord._
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
  def load(addressType: AddressType) =
    div(id := s"addressFieldsDiv${addressType.id}" , "addressUuid".attr := "0")(

      div(id := s"streetAddressDiv${addressType.id}", cls := "form-group")(
        label(cls := "col-lg-3 control-label")("Street Address:"),
        div(cls := "col-lg-8")(
          div(id := s"streetAddressHelpBlock${addressType.id}"),
          input(id := s"streetAddress${addressType.id}", name := "streetAddress", cls := "form-control", `type` := "text")
        )
      ),
      div(id := s"stateDiv${addressType.id}", cls := "form-group")(
        label(cls := "col-lg-3 control-label")("State:"),
        div(cls := "col-lg-8")(
          div(id := s"stateHelpBlock${addressType.id}"),
          div(cls := "ui-select")(
            select(id := s"userState${addressType.id}", name := "userState", cls := "form-control partOfStateList")(
              option(value := emptyAddress.state.id, selected := "selected")(emptyAddress.state.description)
            )
          )
        )
      ),
      div(id := s"zipCodeDiv${addressType.id}", cls := "form-group")(
        label(cls := "col-lg-3 control-label")("Zip code:"),
        div(cls := "col-lg-8")(
          div(id := s"zipCodeHelpBlock${addressType.id}"),
          input(id := s"zipCode${addressType.id}", name := "zipCode", cls := "form-control partOfZipCodeList", `type` := "text")
        )
      )
    )

  def setViolationPrompts(violations: Set[Violation], addressType: AddressType) = {
    if (violations.nonEmpty) {
      //                      val content = dom.document.getElementById("content")
      //                      for (v <- failureList)
      //                        content.appendChild(p(v.description).render)
      violations.foreach(violation =>
        violation.description.getOrElse("") match {
          case street if street.contains("street") =>
            HtmlHelper.showHelpBlock(s"#streetAddressDiv${addressType.id}", s"streetAddressHelpBlock${addressType.id}", "invalid street address format")
          case state if state.contains("state") =>
            HtmlHelper.showHelpBlock(s"#stateDiv${addressType.id}", s"stateHelpBlock${addressType.id}", "please choose a state")
          case zipCode if zipCode.contains("zipCode") =>
            HtmlHelper.showHelpBlock(s"#zipCodeDiv${addressType.id}", s"zipCodeHelpBlock${addressType.id}", "invalid zip code format")
        }
      )
    }
  }

  def buildObjectFromForm(addressType: AddressType): Address = {
    val addressUuid = $(s"#addressFieldsDiv${addressType.id}").attr("addressUuid").toString.trim
    val streetAddress = $(s"#streetAddress${addressType.id}").value().toString.trim
    val stateId = $(s"#userState${addressType.id}").value().toString.trim
    val zipCode = $(s"#zipCode${addressType.id}").value().toString.trim

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
    $(s"#addressFieldsDiv${someAddress.addressType.id}").attr("addressUuid", someAddress.id)
    $(s"#streetAddress${someAddress.addressType.id}").value(someAddress.street.getOrElse(""))
    $(s"#userState${someAddress.addressType.id}").value(someAddress.state.id).change()
    $(s"#zipCode${someAddress.addressType.id}").value(someAddress.zipCode)
  }

  def resetFields(addressType: AddressType) = {
    $(s"#addressFieldsDiv${addressType.id}").attr("addressUuid", "0")
    $(s"#streetAddress${addressType.id}").value("")
    $(s"#userState${addressType.id}").value("NONE").change()
    $(s"#zipCode${addressType.id}").value("")
  }
}

object AddressForm extends AddressForm
// $COVERAGE-ON$