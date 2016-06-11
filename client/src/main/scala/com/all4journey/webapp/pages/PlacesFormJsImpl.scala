package com.all4journey.webapp.pages

import com.all4journey.shared.domain._
import com.all4journey.webapp.util.{HtmlHelper, AddressForm, NavPills, AjaxHelper}
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.Element
import org.scalajs.jquery.{jQuery => $}
import prickle.{Pickle, Unpickle}

import scala.collection.mutable.Set
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._


/**
  * Created by aabreu on 1/30/16.
  */
object PlacesFormJsImpl extends PlacesFormJs with AddressTypePickler {

  val AddNewPlaceIndicator = "Add New Place"

  val placeNames = Set.empty[String]

  def run(): Unit = {}

  def runWithParams(params: Any): Unit = {
    val formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(successPlacesFormData: PlacesFormData) => successPlacesFormData
      case _ => throw new IllegalStateException("the backend didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(placesForm.render)

    buildStatesDropDown(formData.states)
    buildPlacesForm(None, formData.addresses)

    $("#successBanner").hide()
    $("#errorBanner").hide()
  }

  @JSExport
  def buildStatesDropDown(states: Seq[State]): Unit = {

    for (stateItem <- states) {
      val option = buildDropwDownOption(stateItem.description, stateItem.id)
      $(".partOfStateList").append(option)
    }

  }

  @JSExport
  def buildPlacesForm(addressToDisplay: Option[Address], addresses: Seq[Address]): Unit = {

    val homeAddressOption = addresses.find(address => address.addressType == HomeAddressType)
    homeAddressOption.foreach(
      homeAddress => AddressForm.refreshFields(homeAddress)
    )

    val places = addresses.filter(
      address => address.addressType == PlaceAddressType
    )

    placeNames.clear
    places.map(
      placeNames += _.placeName
    )

    val placesDropdown = dom.document.getElementById("places")

    places.foreach(addressItem =>
      buildDropwDownOption(placesDropdown, addressItem.placeName, addressItem.id)
    )

    buildDropwDownOption(placesDropdown, AddNewPlaceIndicator, "0")

    val defaultPlaceOption = addressToDisplay match {
      case Some(address) if address.addressType == PlaceAddressType => addressToDisplay
      case _ => places.headOption
    }

    defaultPlaceOption.foreach(defaultPlace => {
      AddressForm.refreshFields(defaultPlace)
      $("#placeName").value(defaultPlace.placeName)
      $("#places").value(defaultPlace.id).change()
    })

  }

  @JSExport
  def buildDropwDownOption(dropDown: Element, textContent: String, optionIndex: String): Unit = {
    val option = buildDropwDownOption(textContent, optionIndex)
    dropDown.appendChild(option)
  }

  @JSExport
  def buildDropwDownOption(textContent: String, optionIndex: String): Element = {
    val option = dom.document.createElement("option")
    option.textContent = textContent
    option.setAttribute("value", optionIndex)
    option
  }

  @JSExport
  def placesForm: TypedTag[Div] = div(cls := "container")(
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
          h3("Home Address"),
          AddressForm.load(HomeAddressType),
            h3("Your Places"),
            div(id := "placeFieldsDiv")(
              div(id := "placeDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place:"),
                div(cls := "col-lg-8")(
                  div(cls := "ui-select")(
                    select(id := "places", name := "places", cls := "form-control partOfPlacesList", onchange := { () =>
                      val placeId = $("#places").value().toString.trim
                      placeId match {
                        case "0" =>
                          $("#placeName").value("")
                          AddressForm.resetFields(PlaceAddressType)
                        case _ =>
                          AjaxHelper.doAjaxGetWithJson(s"/multiformProfile/places/get?id=$placeId", "", "", AddressForm.refresh, HtmlHelper.showErrorBanner)
                          $("#placeName").value(
                            $("#places option:selected").text.toString.trim
                          )
                      }
                      // if help blocks have been added due to validation, remove them from the form.
                      $(".has-error").removeClass("has-error")
                      $(".help-block").remove()
                    })(
                      //option(value := AddNewPlaceIndicator, selected := "selected")(AddNewPlaceIndicator)
                    )
                  )
                )
              ),
              div(id := "placeNameDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place Name:"),
                div(cls := "col-lg-8")(
                  div(id := "placeNameHelpBlock"),
                  input(id := "placeName", name := "placeName", cls := "form-control", `type` := "text")
                )
              ),
              AddressForm.load(PlaceAddressType)
            )
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save", onclick := { () =>

                  $(".has-error").removeClass("has-error")
                  $(".help-block").remove()

                  val placeAddressUuid = $("#places").value().toString.trim
                  val aPlacename = $("#placeName").value().toString.trim

                  val isPlacenameValid = (placeAddressUuid, aPlacename) match {
                    case ("0",  pn) if pn.nonEmpty => !placeNames.contains(pn)
                    case (anyUuid, "") => false
                    case _ =>
                      val placeNameFromDropDown = $("#places option:selected").text().toString.trim
                      if (aPlacename != placeNameFromDropDown)
                        !placeNames.contains(aPlacename)
                      else
                        true
                  }

                  if (!isPlacenameValid)
                    setPlacenameViolationPrompt()

                  var placeAddress = AddressForm.buildObjectFromForm(PlaceAddressType)
                  placeAddress = placeAddress.copy(id = placeAddressUuid, addressType = PlaceAddressType, placeName = aPlacename)

                  val placeAddressViolations = Address.doValidation(placeAddress)
                  AddressForm.setViolationPrompts(placeAddressViolations, PlaceAddressType)

                  //-----------------

                  val homeAddress = AddressForm.buildObjectFromForm(HomeAddressType)

                  val homeAddressViolations = Address.doValidation(homeAddress)
                  AddressForm.setViolationPrompts(homeAddressViolations, HomeAddressType)

                  if (placeAddressViolations.isEmpty && homeAddressViolations.isEmpty && isPlacenameValid) {
                    val placesFormPayload = new PlacesFormData(Some(homeAddress), Some(placeAddress), Seq.empty[Address], Seq.empty[State])
                    val pickledPfp = Pickle.intoString(placesFormPayload)

                    AjaxHelper.doAjaxPostWithJson("/multiformProfile/places", pickledPfp, "", refreshFormAndPlacesDropDown, HtmlHelper.showErrorBanner)



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
        buildPlacesForm(someFormData.place, someFormData.addresses)
        //$("#places").value(someFormData.address.getOrElse(AddressForm.emptyAddress).id).change()
        HtmlHelper.showSuccessBanner()
      case _ =>
        HtmlHelper.showErrorBanner()
    }
  }

  private def setPlacenameViolationPrompt(): Unit = {
    HtmlHelper.showHelpBlock("#placeNameDiv", "placeNameHelpBlock", "invalid place name... please choose a different one")
  }
}
