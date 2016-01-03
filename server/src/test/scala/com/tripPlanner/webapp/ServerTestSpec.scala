package com.tripPlanner.webapp

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, FlatSpec}


/**
  * Created by rjkj on 1/1/16.
  */
trait ServerTestSpec extends FlatSpec with Matchers with ScalatestRouteTest
