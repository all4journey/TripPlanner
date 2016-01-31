package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{PlacesFormData, State, Address}
import com.tripPlanner.webapp.util.{AjaxHelper, NavPills}
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import prickle.{Pickle, Unpickle}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Success
import scalatags.JsDom.all._

/**
  * Created by aabreu on 1/30/16.
  */
object PlacesFormJsImpl extends PlacesFormJs with NavPills {

  @JSExport
  val emptyAddress = Address("0", "0", None, State("NONE", "Choose a state"), "", "PLACE", "")

  def run(params: PlacesFormData): Unit = {}

  def runWithParams(params: Any): Unit = {
    val formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(successPlacesFormData: PlacesFormData) => successPlacesFormData
      case _ => throw new IllegalStateException("the backend didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(personalInfoForm(formData.address).render)

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

    for (addressItem <- addresses) {
      val option = dom.document.createElement("option")
      option.textContent = addressItem.placeName
      option.setAttribute("value", addressItem.id)
      placesDropdown.appendChild(option)
    }
  }

  @JSExport
  def personalInfoForm(place: Option[Address]) = div(cls := "container")(
    div(cls := "row-fluid")(
      div(cls := "col-sm-12 col-sm-offset-4")(
        getNavPills("placesLink")
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
            div(id := "placeFieldsDiv", "addressUuid".attr := place.getOrElse(emptyAddress).id)(
              div(id := "placeDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place:"),
                div(cls := "col-lg-8")(
                  div(cls := "ui-select")(
                    select(id := "places", name := "places", cls := "form-control partOfStateList", onchange := { () =>
                      val placeId = $("#places").value().toString.trim
                      if (!placeId.equals("New Place")) {
                        AjaxHelper.doAjaxGetWithJson(s"/multiformProfile/places/get?id=$placeId", "", refreshForm, showErrorBanner)
                      } else {
                        $("#placeName").value("")
                        $("#streetAddress").value("")
                        $("#userState").value("NONE").change()
                        $("#zipCode").value("")
                      }
                    })(
                      option(value := "New Place", selected := "selected")("New Place")
                    )
                  )
                )
              ),
              div(id := "placeNameDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Place Name:"),
                div(cls := "col-lg-8")(
                  input(id := "placeName", name := "placeName", cls := "form-control", `type` := "text", value := place.getOrElse(emptyAddress).placeName)
                )
              ),
              div(id := "streetAddressDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Street Address:"),
                div(cls := "col-lg-8")(
                  input(id := "streetAddress", name := "streetAddress", cls := "form-control", `type` := "text", value := place.getOrElse(emptyAddress).street.getOrElse(""))
                )
              ),
              div(id := "stateDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("State:"),
                div(cls := "col-lg-8")(
                  div(cls := "ui-select")(
                    select(id := "userState", name := "userState", cls := "form-control partOfStateList")(
                      option(value := place.getOrElse(emptyAddress).state.id, selected := "selected")(place.getOrElse(emptyAddress).state.description)
                    )
                  )
                )
              ),
              div(id := "zipCodeDiv", cls := "form-group")(
                label(cls := "col-lg-3 control-label")("Zip code:"),
                div(cls := "col-lg-8")(
                  input(id := "zipCode", name := "zipCode", cls := "form-control partOfZipCodeList", `type` := "text", value := place.getOrElse(emptyAddress).zipCode)
                )
              )
            )
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save", onclick := { () =>

                  val addressUuid = $("#placeFieldsDiv").attr("addressUuid").toString.trim
                  val streetAddress = $("#streetAddress").value().toString.trim
                  val stateId = $("#userState").value().toString.trim
                  val zipCode = $("#zipCode").value().toString.trim

                  val address = new Address(addressUuid, "0", Some(streetAddress), State(stateId, ""), zipCode, "HOME", "Home")

                  val placesFormPayload = new PlacesFormData(Some(address), Seq[Address](), Seq[State]())
                  val pickledPfp = Pickle.intoString(placesFormPayload)

                  val placeAction = $("#places").value().toString.trim
                  if (placeAction.equals("New Place")) {
                    AjaxHelper.doAjaxPostWithJson("/multiformProfile/places/new", pickledPfp, refreshForm, showErrorBanner)
                  } else {
                    AjaxHelper.doAjaxPostWithJson("/multiformProfile/places/update", pickledPfp, refreshForm, showErrorBanner)
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

  private def refreshForm(data: js.Any): Unit = {
//    val content = dom.document.getElementById("content")
//    content.appendChild(p(s"$data").render)
    Unpickle[Address].fromString(s"$data") match {
      case Success(someAddress) =>
        $("#placeName").value(someAddress.placeName)
        $("#streetAddress").value(someAddress.street.getOrElse(""))
        $("#userState").value(someAddress.state.id).change()
        $("#zipCode").value(someAddress.zipCode)
      case _ =>
        $("#errorBanner").show()
    }
  }

  private def showErrorBanner(): Unit = {
    $("#errorBanner").show()
  }
}
