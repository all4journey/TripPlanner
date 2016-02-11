package com.all4journey.webapp.util

import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}

import scala.scalajs.js
// $COVERAGE-OFF$
/**
  * Created by aabreu on 1/10/16.
  */
trait AjaxHelper {

  def doAjaxGet(partialUrl: String, contentType: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = "get",
      contentType = contentType,
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

  def doAjaxCall(partialUrl: String, httpMethod: String, dataPayload: String, contentType: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = httpMethod,
      data = dataPayload,
      contentType = contentType,
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

  def doAjaxPostWithJson(partialUrl: String, dataPayload: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    doAjaxCall(partialUrl, "post", dataPayload, "application/json; charset=utf-8", doOnSuccess, doOnFailure)
  }

  def doAjaxGetWithJson(partialUrl: String, dataPayload: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    doAjaxCall(partialUrl, "get", dataPayload, "application/json; charset=utf-8", doOnSuccess, doOnFailure)
  }

}

object AjaxHelper extends AjaxHelper

// $COVERAGE-ON$