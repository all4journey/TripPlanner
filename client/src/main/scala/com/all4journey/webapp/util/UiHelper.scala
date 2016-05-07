package com.all4journey.webapp.util

import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}

import scala.scalajs.js
import scalatags.JsDom.all._

// $COVERAGE-OFF$
/**
  * Created by aabreu on 1/10/16.
  */
trait AjaxHelper {

  def doAjaxGet(partialUrl: String, contentType: String, token:String="", doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = "get",
      contentType = contentType,
      beforeSend = { (jqXHR: JQueryXHR) =>
        jqXHR.setRequestHeader("Token", token)
      },
      traditional = true,
      success = { (data: js.Any, jqXHR: JQueryXHR) =>
        //                     val content = dom.document.getElementById("content")
        // content.appendChild(p(s"$data").render)
        //$("#currentForm").append(data)

        doOnSuccess(data)
      },
      error = { () =>
        doOnFailure
      }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  def doAjaxCall(partialUrl: String, httpMethod: String, dataPayload: String, contentType: String, token:String="", doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = httpMethod,
      data = dataPayload,
      contentType = contentType,
      traditional = true,
      beforeSend = { (jqXHR: JQueryXHR) =>
        jqXHR.setRequestHeader("Token", token)
      },
      success = { (data: js.Any, jqXHR: JQueryXHR) =>
        //                     val content = dom.document.getElementById("content")
        // content.appendChild(p(s"$data").render)
        //$("#currentForm").append(data)

        doOnSuccess(data)
      },
      error = { () =>
        doOnFailure
      }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  def doAjaxPostWithJson(partialUrl: String, dataPayload: String, token:String="", doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    doAjaxCall(partialUrl, "post", dataPayload, "application/json; charset=utf-8", token, doOnSuccess, doOnFailure)
  }

  def doAjaxGetWithJson(partialUrl: String, dataPayload: String, token:String="", doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    doAjaxCall(partialUrl, "get", dataPayload, "application/json; charset=utf-8", token, doOnSuccess, doOnFailure)
  }

}

object AjaxHelper extends AjaxHelper

trait HtmlHelper {
  def showHelpBlock(divName: String, helpBlockName: String, helpBlockMessage: String): Unit = {
    val containsErrorClass = $(divName).hasClass("has-error")
    if (!containsErrorClass) {
      $(divName).addClass("has-error")
      val helpBlock = dom.document.getElementById(helpBlockName)
      helpBlock.appendChild(span(cls := "help-block")(helpBlockMessage).render)
    }
  }

  def showErrorBanner(): Unit = {
    $("#errorBanner").show()
    $("#successBanner").hide()
  }

  def showSuccessBanner(): Unit = {
    $("#successBanner").show()
    $("#errorBanner").hide()
  }
}

object HtmlHelper extends HtmlHelper



// $COVERAGE-ON$