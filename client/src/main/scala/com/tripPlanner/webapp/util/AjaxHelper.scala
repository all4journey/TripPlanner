package com.tripPlanner.webapp.util

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

  def doAjaxPost(partialUrl: String, dataPayload: String, contentType: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = "post",
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

  def doAjaxCallWithJson(partialUrl: String, dataPayload: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    doAjaxPost("/multiformProfile/personal", dataPayload, "application/json; charset=utf-8", doOnSuccess, doOnFailure)
  }

}

object AjaxHelper extends AjaxHelper

// $COVERAGE-ON$