package com.tripPlanner.domain

import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

import scala.concurrent.{ExecutionContext, Future}

import com.tripPlanner.domain.Tables._
import slick.driver.MySQLDriver.api._

/**
  * Created by rjkj on 12/9/15.
  */
trait UserDao {
  def save(user:User): Future[Long]
}

case class UserDaoImpl(db:Database)(implicit ec:ExecutionContext) extends UserDao {
  def save(user:User) = {
    val date:LocalDate = user.registrationDate match {
      case Some(date:ZonedDateTime) => date.toLocalDate
      case None => LocalDate.now()
    }
    val insertUser = users += UserRow(user.id, user.fName, user.lName, java.sql.Date.valueOf(date))

    db.run(insertUser) map {
      result => result
    }
  }

}