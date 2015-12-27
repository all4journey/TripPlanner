package com.tripPlanner.domain

import java.time.ZonedDateTime
import java.util.UUID

/**
  * Created by rjkj on 11/14/15.
  */


case class User(id:String, fName:String = "Default", lName:String, registrationDate: Option[ZonedDateTime])

case class Profile(user: User, addresses: Seq[Address], vehicles: Seq[Vehicle])

case class Address(userId: String, street: String, state: State, zipCode: String)

case class State(id: String, description: String, timezone: Timezone)

case class Timezone(id: String, description: String)

case class Vehicle(userId: String, year: String, make: String, model: String)
