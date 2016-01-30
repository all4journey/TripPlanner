package com.all4journey.domain

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by rjkj on 1/11/16.
  */
class StatesDaoSpec extends DomainTestSpec{
  "StatesDao" should "get a list of states" in {
    val dao = StateDaoImpl(db)
    val result = dao.getStates

    val states = Await.result(result, Duration.Inf)
    states.size shouldEqual 50
  }
}
