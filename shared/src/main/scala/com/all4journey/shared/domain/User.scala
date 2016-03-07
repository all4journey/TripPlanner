package com.all4journey.shared.domain

import com.wix.accord.dsl._

/**
  * Created by rjkj on 11/14/15.
  */


case class User(id: String = "", fName:String = "Default", lName:String, email: String, registrationDate: Option[String])

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
  implicit val addressValidator = validator[Address] { addressToValidate =>
    // this code doesn't work quite right for some reason, it chokes on empty addresses (which need to be valid as to not require the user to enter them)...
    addressToValidate.street match {
      // if a street address is entered then the street address, state and zipcode values must match the validation criteria below
      case Some(streetAddress) =>
        // for some reason streetAddress couldn't be used for validation so I had to call .get here
        //addressToValidate.street.get should matchRegex("""[0-9]?[A-Za-z0-9\s]?""")
        addressToValidate.state.id.length should be == 2
        addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")
      case None =>
        // if a street address is not entered then there are two options, if a state is chosen then a zipcode must be entered, if no state is chosen, the user can still
        // enter a zipcode or leave it empty
        addressToValidate.state.id match {
          case "NONE" =>
            (addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")) or (addressToValidate.zipCode is equalTo(""))
          case _ =>
            addressToValidate.state.id.length should be == 2
            addressToValidate.zipCode should matchRegex("^\\d{5}(?:[-\\s]\\d{4})?$")
        }
    }
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


