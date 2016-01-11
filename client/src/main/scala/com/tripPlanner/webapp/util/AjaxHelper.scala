package com.tripPlanner.webapp.util

import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}

import scala.scalajs.js

/**
  * Created by aabreu on 1/10/16.
  */
trait AjaxHelper {

  def doAjaxCall(partialUrl: String, doOnSuccess: (js.Any) => Unit, doOnFailure: () => Unit): Unit = {
    $.ajax(js.Dynamic.literal(
      url = partialUrl,
      `type` = "get",
      contentType = "text/html; charset=utf-8",
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

}

object AjaxHelper extends AjaxHelper
