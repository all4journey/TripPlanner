package com.tripPlanner.webapp.util

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.tripPlanner.webapp.ServerTestSpec
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by rjkj on 12/19/15.
  */
class LogAppenderSpec extends ServerTestSpec with LogAppenderJsonSupport{
  val route = LogAppender()

  "Log appender" should "log an info statement" in {
    val list = List(
      LogMessage("hello", 1234, "TRACE", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "DEBUG", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "INFO", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "WARN", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "ERROR", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "FATAL", "someUrl", "testMessage")
    )

    Post("/", list) ~> route ~> check{
      status shouldBe OK
    }
  }
}
