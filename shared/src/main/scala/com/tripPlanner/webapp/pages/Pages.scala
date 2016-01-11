package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{Vehicle, State}
import com.tripPlanner.webapp.{JsModule, JsModuleWithParams}

trait ProfileJs extends JsModuleWithParams {
  type ParamType = Seq[State]
}

trait AjaxProfileJs extends JsModule

trait AjaxPasswordChangeFormJs extends JsModuleWithParams {
  type ParamType = String
}

trait AjaxVehicleInfoFormJs extends JsModuleWithParams {
  type ParamType = Vehicle
}

trait AjaxPersonalInfoFormJs extends JsModuleWithParams {
  type ParamType = Seq[State]
}
trait IndexJs extends JsModule