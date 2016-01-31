package com.all4journey.shared.domain.security

import java.util.UUID

/**
  * Created by rjkj on 1/25/16.
  */
case class Token(id: String = "", userId:String = "", token: String = UUID.randomUUID().toString.replaceAll("-", "")){

}

object Token {
  def empty = Token("", "", "")
}
