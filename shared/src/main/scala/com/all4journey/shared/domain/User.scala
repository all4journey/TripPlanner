package com.all4journey.shared.domain

import com.github.t3hnar.bcrypt._
import com.wix.accord._
import com.wix.accord.dsl._
import prickle.{CompositePickler, PicklerPair}
import com.wix.accord.{Success => ValidationSuccess}

case class User(id: String = "", fName:String = "Default", lName:String, email:String, password:String, registrationDate: Option[String]) {
  def withHashedPassword(): User = this.copy(password = password.bcrypt)
}

sealed abstract class AddressType(var id: String, var description: String) {
  override def equals(anyAddressType: Any) = anyAddressType match {
    case thatAddressType: AddressType => thatAddressType.id.equals(this.id) && thatAddressType.description.equals(this.description)
    case _ => false
  }
}

case object HomeAddressType extends AddressType("HOME", "Primary Home Address")
case object PlaceAddressType extends AddressType("PLACE", "An Arbitrary place that you love")

object AddressTypeFactory {
  def buildAddressTypeFromString(addressTypeAsString: String): AddressType = addressTypeAsString match {
    case "HOME" => HomeAddressType
    case "PLACE" => PlaceAddressType
    case _ => throw new IllegalArgumentException("the string passed to the factory does not map to any address type")
  }
}

case class Address(id: String = "", userId: String = "", street: Option[String], state: State, zipCode: String, addressType: AddressType , placeName: String)

case class State(id: String, description: String)

case class Vehicle(id: String = "", userId: String = "", year: Option[String], make: Option[String], model: Option[String])




// front end payload objects

case class PersonalFormData(user: User)

case class PlacesFormData(homeAddress: Option[Address] = None, place: Option[Address] = None, addresses: Seq[Address], states: Seq[State])

case class Profile(user: User, addresses: Seq[Address], vehicles: Seq[Vehicle])

// end payload objects




// validation stuff is below this point

case object User {
  implicit val userValidator = validator[User] { userToValidate =>
    userToValidate.fName.length should be < 50
    userToValidate.lName.length should be < 50
    userToValidate.email.length should be < 50
    userToValidate.email should matchRegex("""^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""")
  }
}

case object Address {
  val StreetRegex = "^\\d{1,}\\s{1}[A-Za-z0-9\\s]?"
  val ZipRegex = "^\\d{5}(?:[-\\s]\\d{4})?$"
  val EmptyStateId = "NONE"

  // if a street address is entered then the street address, state and zipcode values must match the validation criteria below
  val validatorWithStreet = validator[Address] { addressToValidate =>
    addressToValidate.street.getOrElse("").length should be > 0
    addressToValidate.street.getOrElse("") should matchRegex(StreetRegex)
    addressToValidate.state.id.length should be == 2
    addressToValidate.zipCode should matchRegex(ZipRegex)
  }

  /**
    * if a street address is not entered then there are two options, if a state is chosen then a zipcode must be entered, if no state is chosen, the user can still
     enter a zipcode or leave it empty
    */

  val validatorNoStreetNoStateWithZip = validator[Address] { addressToValidate =>
    addressToValidate.street.getOrElse("").length should be == 0
    addressToValidate.state.id is equalTo(EmptyStateId)
    addressToValidate.zipCode should matchRegex(ZipRegex)
  }

  val validatorNoStreetWithState = validator[Address] { addressToValidate =>
    addressToValidate.street.getOrElse("").length should be == 0
    addressToValidate.state.id.length should be == 2
    addressToValidate.zipCode should matchRegex(ZipRegex)
  }

  def doValidation(address: Address): Set[Violation] = {
    //val violations = Set.empty[Violation]
    val fullAddressValidationResult = validate(address)(Address.validatorWithStreet)

    if (fullAddressValidationResult.isFailure) {
      val partialAddressValidationResult = if (address.street.getOrElse("").isEmpty && !address.state.id.equals("NONE"))
        validate(address)(Address.validatorNoStreetWithState)
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && (!address.zipCode.isEmpty))
        validate(address)(Address.validatorNoStreetNoStateWithZip)
      else if (address.street.getOrElse("").isEmpty && address.state.id.equals("NONE") && address.zipCode.isEmpty)
      // an empty address is a valid address
        ValidationSuccess

      partialAddressValidationResult match {
        case ValidationSuccess   => Set.empty[Violation]
        case Failure(failureSet) => failureSet
        case _ => fullAddressValidationResult.asInstanceOf[Failure].violations
      }
    }

    else
      Set.empty[Violation]
  }
}

// end validation stuff is below this point



// prickle stuff
trait AddressTypePickler {
  implicit val addressTypePickler: PicklerPair[AddressType] = CompositePickler[AddressType].concreteType[HomeAddressType.type].
    concreteType[PlaceAddressType.type]
}


