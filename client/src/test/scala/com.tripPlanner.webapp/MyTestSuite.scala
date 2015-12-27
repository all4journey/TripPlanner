package com.tripPlanner.webapp

import java.time.LocalDateTime

import utest._
import utest.ExecutionContext.RunNow

import upickle.default._

import scala.concurrent.Future

case class object1(num:Int, str:Option[String])
case class ello(id:Int, stuff:Seq[object1])

object MyTestSuite extends TestSuite {
  override def tests = TestSuite {
    'test {
      Future {
        assert(true)
      }
    }
    'upickle{
      val tst = ello(1234, Seq(object1(12345, None)))

      val result = write(tst)

      val de = read[ello](result)

      assert(tst.id == de.id)
    }
  }

  tests.runAsync().map( results => {
    assert(results.toSeq.head.value.isSuccess)
    assert(results.toSeq(2).value.isSuccess)
  })

}