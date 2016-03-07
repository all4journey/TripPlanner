package com.all4journey.shared.domain

import com.all4journey.shared.SharedTestSpec
import com.wix.accord._

class ValidationSpec extends SharedTestSpec {

  val validAddressEmpty = Address("0", "0", Some(""), State("NONE", ""), "", "HOME", "Home")

  "Address validator" should "succeed on a valid address" in {
      validate(validAddressEmpty) shouldBe Success
  }
}