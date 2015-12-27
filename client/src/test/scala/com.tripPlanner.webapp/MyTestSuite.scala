package com.tripPlanner.webapp
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