package com.all4journey.webapp.util

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

// $COVERAGE-OFF$
trait NavPills {
  @JSExport
  def getNavPills(active:String) = ul(cls := "nav nav-pills")(
    li(id := "personalInfoLink", role := "presentation", if(active.equalsIgnoreCase("personalInfoLink")) cls := "active" else "")(
      a(href := "/multiformProfile/personal")("Personal Info")
    ),
    li(id := "vehicleInfoLink", role := "presentation", if(active.equalsIgnoreCase("vehicleinfolink")) cls := "active" else "")(
      a(href := "/multiformProfile/vehicle")("Vehicle Info")
    ),
    li(id := "passwordChangeLink", role := "presentation", if(active.equalsIgnoreCase("passwordChangeLink")) cls := "active" else "")(
      a(href := "/multiformProfile/password")("Change Password")
    )
  )
}
// $COVERAGE-ON$