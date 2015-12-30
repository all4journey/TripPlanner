package com.tripPlanner.domain

import slick.driver.MySQLDriver.api._
import com.tripPlanner.shared.domain.State
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import com.tripPlanner.domain.Tables.{UsState => states}

/**
  * Created by aabreu on 12/29/15.
  */
trait StateDao {
  def getStates(): Seq[State]
}

case class StateDaoImpl(db: Database)(implicit ec: ExecutionContext) extends StateDao {
  def getStates(): Seq[State] = {

    // hard coding this list for now... it should come out of the DB
    val newYork = State("NY", "New York")
    val georgia = State("GA", "Georgia")
    val pen = State("PA", "Pensylvannia")

    return Seq(newYork, georgia, pen)
  }
}
