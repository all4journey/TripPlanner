package com.all4journey.webapp

/**
  * Created by aabreu on 5/29/16.
  */
package object exceptions {
  case object InvalidAddressException extends IllegalArgumentException("The address passed to the server is invalid")
  case object HomeAddressException extends IllegalStateException("There is a home address for this user already")
  case object MultipleHomeAddressException extends IllegalStateException("There is more than one home address")
  case object NoAddressException extends IllegalArgumentException("there must be an input address in order to process this request")
}
