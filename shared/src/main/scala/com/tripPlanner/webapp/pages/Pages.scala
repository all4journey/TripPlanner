package com.tripPlanner.webapp.pages

import com.tripPlanner.shared.domain.State
import com.tripPlanner.webapp.{JsModule, JsModuleWithParams}

trait ProfileJs extends JsModuleWithParams {
  type ParamType = Seq[State]
}
trait IndexJs extends JsModule