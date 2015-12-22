package com.tripPlanner.domain

import java.time.ZonedDateTime
import java.util.UUID

/**
  * Created by rjkj on 11/14/15.
  */
case class User(id:String, fName:String = "Default", lName:String, registrationDate: Option[ZonedDateTime])

case class Profile(firstName: String, lastName: String, company: String, userTimezone: String, streetAddress: String, userState: String, zipCode: String, userVehicleYear: String, make: String, model: String)