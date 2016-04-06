package com.all4journey.shared.domain

import com.all4journey.shared.SharedTestSpec
import com.wix.accord._

class ValidationSpec extends SharedTestSpec {

  val invalidAddressWithStreet = Address("0", "0", Some("123 street"), State("NONE", ""), "", "HOME", "Home")

  "Address.validatorWithStreet" should "fail with street address populated but state and zip are empty" in {
      validate(invalidAddressWithStreet)(Address.validatorWithStreet) shouldBe a[Failure]
  }
}