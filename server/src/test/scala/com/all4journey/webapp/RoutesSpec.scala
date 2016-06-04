package com.all4journey.webapp

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._

import com.all4journey.webapp.util.{LogAppenderJsonSupport, LogMessage}



import scala.language.implicitConversions

/**
  * Created by rjkj on 1/1/16.
  */
class RoutesSpec extends ServerTestSpec with LogAppenderJsonSupport{
  val route = Routes()

  "Routes" should "load indexPage" in {
    Get("/") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/html(UTF-8)`
    }
  }

  it should "load profilePage" in {
    Get("/profile") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/html(UTF-8)`
    }
  }

  it should "load places form" in {
    Get("/multiformProfile/places") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/html(UTF-8)`
    }
  }

  it should "load personal info form" in {
    Get("/multiformProfile/personal") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/html(UTF-8)`
    }
  }

  it should "load a place by address id" in {
    Get("/multiformProfile/places/get?id=ec0a0374-9cab-47d3-8803-bcf3243ec423") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/plain(UTF-8)`
    }
  }

  it should "load password change form" in {
    Get("/multiformProfile/password") ~> route ~> check {
      status shouldBe OK
      contentType shouldBe `text/html(UTF-8)`
    }
  }

  it should "locate webjars" in {
    Get("/assets/bootstrap/bootstrap.min.js") ~> route ~> check {
      status shouldBe OK
    }
  }

  it should "return not found when webjar does not exist" in {
    Get("/assets/some/junk.js") ~> route ~> check {
      status shouldBe NotFound
    }
  }

  it should "load client-fastopt" in {
    Get("/client-fastopt.js") ~> route ~> check {
      status shouldBe OK
    }
  }

  it should "load client-launcher.js" in {
    Get("/client-launcher.js") ~> route ~> check {
      status shouldBe OK
    }
  }

  it should "load client-jsdeps.js" in {
    Get("/client-jsdeps.js") ~> route ~> check {
      status shouldBe OK
    }
  }

  it should "accept a log Message" in {
    val list = List(
      LogMessage("hello", 1234, "TRACE", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "DEBUG", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "INFO", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "WARN", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "ERROR", "someUrl", "testMessage"),
      LogMessage("hello", 1234, "FATAL", "someUrl", "testMessage")
    )

    Post("/log", list) ~> route ~> check{
      status shouldBe OK
    }
  }
}
