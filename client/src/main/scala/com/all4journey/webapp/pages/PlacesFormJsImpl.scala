package com.all4journey.webapp.pages

import com.all4journey.shared.domain._
import com.all4journey.webapp.util.{HtmlHelper, AddressForm, NavPills, AjaxHelper}
import org.scalajs.dom
import org.scalajs.dom.raw.Element
import org.scalajs.jquery.{jQuery => $}
import prickle.{Pickle, Unpickle}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/30/16.
  */
object PlacesFormJsImpl extends PlacesFormJs with AddressTypePickler {

  val AddNewPlaceIndicator = "Add New Place"

  def run(params: PlacesFormData): Unit = {}

  def runWithParams(params: Any): Unit = {
    val formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(successPlacesFormData: PlacesFormData) => successPlacesFormData
      case _ => throw new IllegalStateException("the backend didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(placesForm(None).render)

    buildStatesDropDown(formData.states)
    buildPlacesDropDown(formData.addresses)

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
  def buildPlacesDropDown(addresses: Seq[Address]): Unit = {

    val placesDropdown = dom.document.getElementById("places")

    // find the HOME place and add it to the top
    var listWithoutHomeType = new ListBuffer[Address]()
      for (addressItem <- addresses) {
        if (addressItem.addressType.equals(HomeAddressType)) {
          buildDropwDownOption(placesDropdown, addressItem.placeName, addressItem.id)

          AddressForm.refreshFields(addressItem)
        }

        else {
          listWithoutHomeType += addressItem
        }
      }


    for (addressItem <- listWithoutHomeType) {
      buildDropwDownOption(placesDropdown, addressItem.placeName, addressItem.id)
    }

    buildDropwDownOption(placesDropdown, AddNewPlaceIndicator, "0")

  }

  @JSExport
  def buildDropwDownOption(dropDown: Element, textContent: String, optionIndex: String): Unit = {
    val option = dom.document.createElement("option")
    option.textContent = textContent
    option.setAttribute("value", optionIndex)
    dropDown.appendChild(option)
  }

  @JSExport
  def placesForm(defaultAddress: Option[Address]) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        NavPills.load("placesLink")
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
            h3("Your Places"),
            div(id := "placeFieldsDiv")(
              div(id := "placeDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place:"),
                div(cls := "col-lg-8")(
                  div(cls := "ui-select")(
                    select(id := "places", name := "places", cls := "form-control partOfStateList", onchange := { () =>
                      val placeId = $("#places").value().toString.trim
                      if (!placeId.equals("0")) {
                        AjaxHelper.doAjaxGetWithJson(s"/multiformProfile/places/get?id=$placeId", "", AddressForm.refresh, HtmlHelper.showErrorBanner)
                      } else {
                        $("#placeName").value("")
                        $("#streetAddress").value("")
                        $("#userState").value("NONE").change()
                        $("#zipCode").value("")
                      }
                    })(
                      //option(value := AddNewPlaceIndicator, selected := "selected")(AddNewPlaceIndicator)
                    )
                  )
                )
              ),
              div(id := "placeNameDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place Name:"),
                div(cls := "col-lg-8")(
                  input(id := "placeName", name := "placeName", cls := "form-control", `type` := "text")
                )
              ),
              AddressForm.load(defaultAddress)
            )
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save", onclick := { () =>

                  $(".has-error").removeClass("has-error")
                  $(".help-block").remove()

                  val addressUuid = $("#places").value().toString.trim
                  val pn = $("#placeName").value().toString.trim

                  var address = AddressForm.buildObjectFromForm()
                  address = address.copy(id = addressUuid, addressType = PlaceAddressType, placeName = pn)

                  val addressViolations = AddressForm.doValidation(address)
                  AddressForm.setViolationPrompts(addressViolations)

                  if (addressViolations.isEmpty) {
                    val placesFormPayload = new PlacesFormData(Some(address), Seq[Address](), Seq[State]())
                    val pickledPfp = Pickle.intoString(placesFormPayload)

                    if (addressUuid.equals("0")) {
                      AjaxHelper.doAjaxPostWithJson("/multiformProfile/places/new", pickledPfp, refreshFormAndPlacesDropDown, HtmlHelper.showErrorBanner)
                    } else {
                      AjaxHelper.doAjaxPostWithJson("/multiformProfile/places/update", pickledPfp, refreshFormAndPlacesDropDown, HtmlHelper.showErrorBanner)
                    }
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

  private def refreshFormAndPlacesDropDown(data: js.Any): Unit = {
    Unpickle[PlacesFormData].fromString(s"$data") match {
      case Success(someFormData: PlacesFormData) =>
        $("#places").empty()
        buildPlacesDropDown(someFormData.addresses)
        $("#places").value(someFormData.address.getOrElse(AddressForm.emptyAddress).id).change()
        HtmlHelper.showSuccessBanner()
      case _ =>
        HtmlHelper.showErrorBanner()
    }
  }
}
