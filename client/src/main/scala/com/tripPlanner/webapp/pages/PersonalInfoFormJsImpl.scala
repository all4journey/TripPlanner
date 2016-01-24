package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{User, PersonalFormData, State, Address}
import com.tripPlanner.webapp.util.{AjaxHelper, NavPills}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import prickle.{Pickle, Unpickle}
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import scala.collection.mutable.ListBuffer
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

  @JSExport
  val emptyAddress = Address("0", "0", None, State("NONE", "Choose a state"), "")

  def run(params: PersonalFormData): Unit = {}

  def runWithParams(params: Any): Unit = {

    formData = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])) match {
      case Success(successPersonalFormData: PersonalFormData) => successPersonalFormData
      case _ => throw new IllegalStateException("the backend didn't send any form data")
    }

    val content = dom.document.getElementById("content")
    content.appendChild(personalInfoForm(formData.user).render)

    if (formData.addressListToAdd.isEmpty) {
      addAddressPanelWithAddButton()
    }
    else {
      for (address <- formData.addressListToAdd) {
        addAddressPanelWithMinusButton(Some(address))
      }

      addAddressPanelWithAddButton()
    }

    $(".btn-add").click(addMoreAddressFields _)
    $("#successBanner").hide()
    $("#errorBanner").hide()
  }

  @JSExport
  def addAddressPanelWithAddButton(): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(addressTextFields(None, "btn btn-success btn-add", "glyphicon glyphicon-plus", "has-success", "Add", 0).render)

    buildStatesDropDown(0)

    //formIndex += 1
  }

  @JSExport
  def addAddressPanelWithMinusButton(address: Option[Address]): Unit = {
    val mainFormGroup = dom.document.getElementById("main-form-group")
    mainFormGroup.appendChild(addressTextFields(address, "btn btn-danger btn-sub", "glyphicon glyphicon-remove", "has-error", "Remove", formIndex).render)

    addPlusOrMinusButtonAction(formIndex)

    buildStatesDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def addPlusOrMinusButtonAction(butonIndex: Int): Unit = {
    $("#plusMinusButton" + butonIndex).click({ (hTMLElement: HTMLElement) =>
      val index = $(hTMLElement).attr("index")
      $("#addressFieldsDiv" + index).attr("actionFlag", "remove")
      $("#addressFieldsDiv" + index).hide()
    } : js.ThisFunction)
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
    val addressUuid = $("#addressFieldsDiv0").attr("addressUuid").toString.trim
    val streetAddress = $("#streetAddress0").value().toString.trim
    val stateId = $("#userState0").value().toString.trim
    val stateDescription = $("#userState0 option:selected").text().toString.trim
    val zipCode = $("#zipCode0").value().toString.trim
    val address = new Address(addressUuid, "0", Some(streetAddress), State(stateId, stateDescription), zipCode)

    $("#addressFieldsDiv0").before(addressTextFields(Some(address), "btn btn-danger btn-sub", "glyphicon glyphicon-remove", "has-error", "Remove", formIndex).render)

    $("#streetAddress0").value("")
    $("#userState0").value("NONE")
    $("#zipCode0").value("")
    $("#streetAddressDiv0").attr("addressUuid", "0")

    addPlusOrMinusButtonAction(formIndex)

    buildStatesDropDown(formIndex)

    formIndex += 1
  }

  @JSExport
  def addressTextFields(address: Option[Address], buttonClass: String, glyphClass: String,  highlightClass:String, toolTipMessage: String, formIndex: Int) =
    div(id := "addressFieldsDiv" + formIndex, "actionFlag".attr := "add", cls := "streetAddressSet", "addressUuid".attr := address.getOrElse(emptyAddress).id, "index".attr := formIndex)(

    div(id := "streetAddressDiv" + formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("Street Address:"),
    div(cls := "col-lg-8")(
      div(cls := "input-group")(
        input(id := "streetAddress" + formIndex, name := "streetAddress" + formIndex, cls := "form-control partOfAddressList", `type` := "text", value := address.getOrElse(emptyAddress).street.getOrElse("")),
        span(cls := "input-group-btn")(
//          if (displayResetAlso) {
//            button(id := "resetButton" + formIndex, cls := "btn btn-primary btn-sub", "data-toggle".attr := "tooltip", title := "Reset", `type` := "button", onmouseover := { () =>
//              $("#streetAddressDiv" + formIndex).addClass("has-error")
//              $("#stateDiv" + formIndex).addClass("has-error")
//              $("#zipCodeDiv" + formIndex).addClass("has-error")
//            }, onmouseout := { () =>
//              $("#streetAddressDiv" + formIndex).removeClass("has-error")
//              $("#stateDiv" + formIndex).removeClass("has-error")
//              $("#zipCodeDiv" + formIndex).removeClass("has-error")
//            }, onclick := { () =>
//              $("#streetAddress" + formIndex).value("")
//              $("#userState" + formIndex).value("NONE")
//              $("#zipCode" + formIndex).value("")
//            },
//              "index".attr := formIndex)(
//              span(cls := "glyphicon glyphicon-repeat", style := "font-size:1.45em;"))
//          } else "",
          button(id := "plusMinusButton" + formIndex, cls := buttonClass, "data-toggle".attr := "tooltip", title := toolTipMessage, `type` := "button", onmouseover := { () =>
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
        select(id := "userState" + formIndex, name := "userState" + formIndex, cls := "form-control partOfStateList")(
          option(value := address.getOrElse(emptyAddress).state.id, selected := "selected")(address.getOrElse(emptyAddress).state.description)
        )
      )
    )
  ),
  div(id := "zipCodeDiv" + formIndex, cls := "form-group")(
    label(cls := "col-lg-3 control-label")("Zip code:"),
    div(cls := "col-lg-8")(
      input(id := "zipCode" + formIndex, name := "zipCode" + formIndex, cls := "form-control partOfZipCodeList", `type` := "text", value := address.getOrElse(emptyAddress).zipCode)
    )
  )
  )

  @JSExport
  def personalInfoForm(user: User) = div(cls := "container")(
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
            )
          ),
          div(
            div(cls := "form-group")(
              label(cls := "col-lg-5 control-label")(),
              div(cls := "col-md-7")(
                input(id := "saveButton", `type` := "button", cls := "btn btn-primary", value := "Save Changes", onclick := { () =>
                  val firstName = $("#firstName").value().toString.trim
                  val lastName =  $("#lastName").value().toString.trim
                  val emailAddress = $("#email").value().toString.trim

                  val user = new User("0", firstName, lastName, emailAddress, None)

                  var addressListToAddOrUpdate = new ListBuffer[Address]()
                  var addressListToRemove = new ListBuffer[Address]()

                  val buildAddressFromForm: js.Function2[js.Any, dom.Element, js.Any] = { (anyTwo: js.Any, element: dom.Element) =>
                    val index = $(element).attr("index")
                    val addressUuid = $(element).attr("addressUuid").toString.trim

                    val streetAddress = $("#streetAddress" + index).value().toString.trim
                    val stateId = $("#userState" + index).value().toString.trim
                    val zipCode = $("#zipCode" + index).value().toString.trim

                    val address = new Address(addressUuid, "0", Some(streetAddress), State(stateId, ""), zipCode)

                    val actionFlag = $(element).attr("actionFlag").toString.trim
                    if (actionFlag.equals("remove") && !addressUuid.equals("0")) {
                      addressListToRemove += address
                      $("#addressFieldsDiv" + index).remove()
                    } else if (actionFlag.equals("add")) {
                      addressListToAddOrUpdate += address
                    }


                    anyTwo
                  }

                  $(".streetAddressSet").each(buildAddressFromForm)

                  val personalFormPayload = new PersonalFormData(user, addressListToAddOrUpdate, addressListToRemove, Seq[State]())
                  val pickledPfp = Pickle.intoString(personalFormPayload)

                  AjaxHelper.doAjaxCallWithJson("/multiformProfile/personal", pickledPfp, showSuccessBanner, showErrorBanner)

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

  private def showSuccessBanner(data: js.Any): Unit = {
    dom.window.location.reload(true);
  }

  private def showErrorBanner(): Unit = {
    $("#errorBanner").show()
  }
}
// $COVERAGE-ON$