package com.all4journey.shared.domain

import com.github.t3hnar.bcrypt._

case class User(id: String = "", fName:String = "Default", lName:String, emailAddress:String, password:String, registrationDate: Option[String]) {
  def withHashedPassword(): User = this.copy(password = password.bcrypt)
}

case class Profile(user: User, addresses: Seq[Address], vehicles: Seq[Vehicle])

case class Address(id: String = "", userId: String = "", street: Option[String], state: State, zipCode: String)

case class State(id: String, description: String)

case class Vehicle(id: String = "", userId: String = "", year: Option[String], make: Option[String], model: Option[String])

// need this to load the personal info section of the profile with the list of addresses
// and also the list of states for the dropdown
// the JsModuleWithParams only takes one Param type so this class is used to wrap up the two
// params I need to load the personal info page
// TODO - JsModuleWithParams should be able to take multiple params
case class PersonalFormData(user:Option[User], addresses: Seq[Address], states: Seq[State])
