package com.all4journey.domain

import slick.driver.MySQLDriver.api._
import com.all4journey.shared.domain.State
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import com.all4journey.domain.Tables.{UsState => states, UsStateRow => row}

/**
  * Created by aabreu on 12/29/15.
  */
trait StateDao {
  def getStates: Future[Seq[State]]
  def getStateById(stateId: String): Future[Seq[State]]
}

case class StateDaoImpl(db: Database)(implicit ec: ExecutionContext) extends StateDao {
  def getStates: Future[Seq[State]] = {

    db.run(states.result) map {
      stateList => for {
        s <- stateList
      } yield State(s.id, s.description.getOrElse("NotFound"))
    }
  }

  def getStateById(stateId: String): Future[Seq[State]] = {
    val query = states.filter(_.id === stateId)

    db.run(query.result) map {
      stateList => for {
        s <- stateList
      } yield State(s.id, s.description.getOrElse("NotFound"))
    }
  }


}
