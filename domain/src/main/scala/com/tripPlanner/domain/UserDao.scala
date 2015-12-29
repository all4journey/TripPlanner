package com.tripPlanner.domain

import java.text.SimpleDateFormat
import java.time._
import java.util.Date

import scala.concurrent.{ExecutionContext, Future}

import slick.driver.MySQLDriver.api._

import com.tripPlanner.shared.domain.User

/**
  * Created by rjkj on 12/9/15.
  */
trait UserDao {
  def save(user:User): Future[Long]
}

case class UserDaoImpl(db:Database)(implicit ec:ExecutionContext) extends UserDao {
  def save(user:User) = {
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val dateFromString:Date = user.registrationDate match {
      case Some(stringDate:String) => dateFormat.parse(stringDate)
      case None => new Date()
    }

    val insertUser = Tables.User += Tables.UserRow(user.id, user.fName, user.lName, new java.sql.Date(dateFromString.getTime))

    db.run(insertUser) map {
      result => result
    }
  }

}