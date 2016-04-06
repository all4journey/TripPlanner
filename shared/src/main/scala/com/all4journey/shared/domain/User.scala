package com.all4journey.shared.domain

import com.github.t3hnar.bcrypt._
import com.wix.accord.dsl._

case class User(id: String = "", fName:String = "Default", lName:String, emailAddress:String, password:String, registrationDate: Option[String]) {
  def withHashedPassword(): User = this.copy(password = password.bcrypt)
}

// the validator needs to go in the same file where the case class is defined otherwise, the compiler throws a fit
case object User {
  implicit val userValidator = validator[User] { userToValidate =>
    userToValidate.fName.length should be < 50
    userToValidate.lName.length should be < 50
    userToValidate.email.length should be < 50
    userToValidate.email should matchRegex("""^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""")
  }
}

case class Profile(user: User, addresses: Seq[Address], vehicles: Seq[Vehicle])

case class Address(id: String = "", userId: String = "", street: Option[String], state: State, zipCode: String, addressType: String , placeName: String)

case object Address {
  // if a street address is entered then the street address, state and zipcode values must match the validation criteria below
  val validatorWithStreet = validator[Address] { addressToValidate =>
    addressToValidate.street.getOrElse("").length should be > 0
    addressToValidate.street.getOrElse("") should matchRegex("^\\d{1,}\\s{1}[A-Za-z0-9\\s]?")
    addressToValidate.state.id.length should be == 2
    addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")
  }

  /**
    * if a street address is not entered then there are two options, if a state is chosen then a zipcode must be entered, if no state is chosen, the user can still
     enter a zipcode or leave it empty
    */

  val validatorNoStreetNoStateWithZip = validator[Address] { addressToValidate =>
    addressToValidate.street.getOrElse("").length should be == 0
    addressToValidate.state.id is equalTo("NONE")
    addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")
  }

  val validatorNoStreetWithState = validator[Address] { addressToValidate =>
    addressToValidate.state.id.length should be == 2
    addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")
  }
}

case class State(id: String, description: String)

case class Vehicle(id: String = "", userId: String = "", year: Option[String], make: Option[String], model: Option[String])

// need this to load the personal info section of the profile with the list of addresses
// and also the list of states for the dropdown
// the JsModuleWithParams only takes one Param type so this class is used to wrap up the two
// params I need to load the personal info page
// TODO - JsModuleWithParams should be able to take multiple params
case class PersonalFormData(user: User, address: Option[Address], states: Seq[State])

case class PlacesFormData(address: Option[Address], addresses: Seq[Address], states: Seq[State])
