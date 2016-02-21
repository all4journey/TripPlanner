package com.all4journey.webapp

import com.all4journey.shared.domain.User
import com.tsukaby.bean_validation_scala.ScalaValidatorFactory

/**
  * Created by aabreu on 2/21/16.
  */

class ValidationTestSpec extends ServerTestSpec {
  "fname" should "be less than 10" in {
    val validator = ScalaValidatorFactory.validator

    val user = User("", "12345678901", "", "", None)
    val violations = validator.validate(user)

    violations.nonEmpty shouldEqual true
  }
}
