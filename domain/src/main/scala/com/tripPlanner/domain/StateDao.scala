package com.tripPlanner.domain

import slick.driver.MySQLDriver.api._
import com.tripPlanner.shared.domain.State
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import com.tripPlanner.domain.Tables.{UsState => states, UsStateRow => row}

/**
  * Created by aabreu on 12/29/15.
  */
trait StateDao {
  def getStates: Future[Seq[State]]
}

case class StateDaoImpl(db: Database)(implicit ec: ExecutionContext) extends StateDao {
  def getStates: Future[Seq[State]] = {

    val statesList = TableQuery[states]

    // hard coding this list for now... it should come out of the DB
//    val newYork = State("NY", "New York")
//    val georgia = State("GA", "Georgia")
//    val pen = State("PA", "Pensylvannia")


    db.run(statesList.result) map {
      stateList => for {
        s <- stateList
      } yield State(s.id, s.description.getOrElse("NotFound"))
    }
  }
}
