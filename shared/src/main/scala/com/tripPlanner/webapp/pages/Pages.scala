package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.{PlacesFormData, PersonalFormData, Vehicle, State}
import com.tripPlanner.webapp.{JsModule, JsModuleWithParams}

trait ProfileJs extends JsModuleWithParams {
  type ParamType = Seq[State]
}

trait MultiFormProfileJs extends JsModule

trait PasswordChangeFormJs extends JsModuleWithParams {
  type ParamType = String
}

trait VehicleInfoFormJs extends JsModuleWithParams {
  type ParamType = Seq[Vehicle]
}

trait PersonalInfoFormJs extends JsModuleWithParams {
  type ParamType = PersonalFormData
}

trait PlacesFormJs extends JsModuleWithParams {
  type ParamType = PlacesFormData
}

trait IndexJs extends JsModule