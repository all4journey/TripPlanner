package com.all4journey.webapp

import java.time.LocalDateTime

import utest._
import utest.ExecutionContext.RunNow

import scala.concurrent.Future

object MyTestSuite extends TestSuite {
  override def tests = TestSuite {
    'test {
      Future {
        assert(true)
      }
    }
  }

  tests.runAsync().map( results => {
    assert(results.toSeq.head.value.isSuccess)
  })

}