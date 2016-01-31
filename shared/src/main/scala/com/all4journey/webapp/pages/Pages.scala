package com.all4journey.webapp.pages

import com.all4journey.shared.domain.{PersonalFormData, Vehicle, State}
import com.all4journey.webapp.{JsModule, JsModuleWithParams}

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
trait IndexJs extends JsModule

trait LoginJs extends JsModule