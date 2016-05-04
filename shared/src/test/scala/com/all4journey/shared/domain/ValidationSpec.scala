package com.all4journey.shared.domain

import com.all4journey.shared.SharedTestSpec
import com.wix.accord._
import com.wix.accord.{Success => ValidationSuccess}

class ValidationSpec extends SharedTestSpec {

  val testUser = User("", "", "", "", "", None)

  "user validation" should "fail with first name greater than 50 characters" in {
    val invalidUserFnameGT50 = testUser.copy(fName = "12345678901234567890123456789012345678901234567890")
    validate(invalidUserFnameGT50) shouldBe a[Failure]
  }

  "user validation" should "fail with last name greater than 50 characters" in {
    val invalidUserLnameGT50 = testUser.copy(lName = "12345678901234567890123456789012345678901234567890")
    validate(invalidUserLnameGT50) shouldBe a[Failure]
  }

  "user validation" should "fail with email address greater than 50 characters" in {
    val invalidUserEmailGT50 = testUser.copy(email = "12345678901234567890123456789012345678901234567890")
    validate(invalidUserEmailGT50) shouldBe a[Failure]
  }

  "user validation" should "fail with invalid email address" in {
    val invalidUserEmail = testUser.copy(email = "1234567890")
    validate(invalidUserEmail) shouldBe a[Failure]
  }

  "user validation" should "pass with a valid email address" in {
    val invalidUserEmail = testUser.copy(email = "a.a@all4journey.com")
    validate(invalidUserEmail) shouldBe ValidationSuccess
  }

  val testAddress = Address("0", "0", None, State("NONE", ""), "", HomeAddressType, "")

  "Address.validatorWithStreet" should "fail with street address set to None" in {
    validate(testAddress)(Address.validatorWithStreet) shouldBe a[Failure]
  }

  "Address.validatorWithStreet" should "fail with an invalid street address format" in {
    val invalidAddress = testAddress.copy(street = Some("123 123"))
    validate(invalidAddress)(Address.validatorWithStreet) shouldBe a[Failure]
  }

  "Address.validatorWithStreet" should "fail with street address populated but state and zip are empty" in {
    val invalidAddress = testAddress.copy(street = Some("123 street"))
    validate(invalidAddress)(Address.validatorWithStreet) shouldBe a[Failure]
  }

  "Address.validatorWithStreet" should "fail with valid street address and state populated but zip is empty" in {
    val invalidAddress = testAddress.copy(street = Some("123 street"), state = State("NY", ""))
    validate(invalidAddress)(Address.validatorWithStreet) shouldBe a[Failure]
  }

  "Address.validatorWithStreet" should "fail with valid street address and state populated but zip is invalid" in {
    val invalidAddress = testAddress.copy(street = Some("123 street"), state = State("NY", ""), zipCode = "11")
    validate(invalidAddress)(Address.validatorWithStreet) shouldBe a[Failure]
  }

  "Address.validatorWithStreet" should "pass with valid street address,state and 5 digit zip" in {
    val validAddress = testAddress.copy(street = Some("123 street"), state = State("NY", ""), zipCode = "12345")
    validate(validAddress)(Address.validatorWithStreet) shouldBe ValidationSuccess
  }

  "Address.validatorWithStreet" should "pass with valid street address,state and 9 digit zip" in {
    val validAddress = testAddress.copy(street = Some("123 street"), state = State("NY", ""), zipCode = "12345-1234")
    validate(validAddress)(Address.validatorWithStreet) shouldBe ValidationSuccess
  }

  "Address.validatorNoStreetNoStateWithZip" should "fail with street field populated" in {
    val invalidAddressWithStreet = testAddress.copy(street = Some("123 street"))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetNoStateWithZip) shouldBe a[Failure]
  }

  "Address.validatorNoStreetNoStateWithZip" should "fail with empty street field but state field populated" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NY", ""))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetNoStateWithZip) shouldBe a[Failure]
  }

  "Address.validatorNoStreetNoStateWithZip" should "fail with empty street field state set to NONE with empty zip" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NONE", ""))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetNoStateWithZip) shouldBe a[Failure]
  }

  "Address.validatorNoStreetNoStateWithZip" should "fail with empty street field state set to NONE with invalid zip" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NONE", ""), zipCode = "11")
    validate(invalidAddressWithStreet)(Address.validatorNoStreetNoStateWithZip) shouldBe a[Failure]
  }

  "Address.validatorNoStreetNoStateWithZip" should "pass with empty street field state set to NONE with 5 digit zip" in {
    val validAddress = testAddress.copy(state = State("NONE", ""), zipCode = "12345")
    validate(validAddress)(Address.validatorNoStreetNoStateWithZip) shouldBe ValidationSuccess
  }

  "Address.validatorNoStreetNoStateWithZip" should "pass with empty street field state set to NONE with 9 digit zip" in {
    val validAddress = testAddress.copy(state = State("NONE", ""), zipCode = "12345-1234")
    validate(validAddress)(Address.validatorNoStreetNoStateWithZip) shouldBe ValidationSuccess
  }



  "Address.validatorNoStreetWithState" should "fail with street field populated" in {
    val invalidAddressWithStreet = testAddress.copy(street = Some("123 street"))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetWithState) shouldBe a[Failure]
  }

  "Address.validatorNoStreetWithState" should "fail with empty street field but state field is set to NONE" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NONE", ""))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetWithState) shouldBe a[Failure]
  }

  "Address.validatorNoStreetWithState" should "fail with empty street field state set but with empty zip" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NY", ""))
    validate(invalidAddressWithStreet)(Address.validatorNoStreetWithState) shouldBe a[Failure]
  }

  "Address.validatorNoStreetWithState" should "fail with empty street field state set with invalid zip" in {
    val invalidAddressWithStreet = testAddress.copy(state = State("NY", ""), zipCode = "11")
    validate(invalidAddressWithStreet)(Address.validatorNoStreetWithState) shouldBe a[Failure]
  }

  "Address.validatorNoStreetWithState" should "pass with empty street field state set with 5 digit zip" in {
    val validAddress = testAddress.copy(state = State("NY", ""), zipCode = "12345")
    validate(validAddress)(Address.validatorNoStreetWithState) shouldBe ValidationSuccess
  }

  "Address.validatorNoStreetWithState" should "pass with empty street field state set with 9 digit zip" in {
    val validAddress = testAddress.copy(state = State("NY", ""), zipCode = "12345-1234")
    validate(validAddress)(Address.validatorNoStreetWithState) shouldBe ValidationSuccess
  }
}