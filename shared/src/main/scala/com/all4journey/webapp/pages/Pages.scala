package com.all4journey.webapp.pages

import com.all4journey.shared.domain.{PlacesFormData, PersonalFormData, Vehicle, State}
import com.all4journey.webapp.{JsModule, JsModuleWithParams}

trait ProfileJs extends JsModuleWithParams {
  type ParamType = Tuple2[String,Seq[State]]
}

trait MultiFormProfileJs extends JsModule

trait PasswordChangeFormJs extends JsModuleWithParams {
  type ParamType = Tuple2[String, String]
}

trait VehicleInfoFormJs extends JsModuleWithParams {
  type ParamType = Tuple2[String, Seq[Vehicle]]
}

trait PersonalInfoFormJs extends JsModuleWithParams {
  type ParamType = Tuple2[String, PersonalFormData]
}

trait PlacesFormJs extends JsModuleWithParams {
  type ParamType = PlacesFormData
}

trait IndexJs extends JsModule

trait LoginJs extends JsModule