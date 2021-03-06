package com.all4journey.webapp

import prickle._
import scala.scalajs.js.annotation.{JSExport, JSExportDescendentObjects}

@JSExportDescendentObjects
trait JsModuleWithParams {
  type ParamType

  @JSExport
  def runWithParams(params: Any): Unit

  def run(): Unit
}