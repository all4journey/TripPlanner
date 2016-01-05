package com.tripPlanner.domain


import java.text.SimpleDateFormat
import java.util.Date

import scala.concurrent.{Await, ExecutionContext, Future}

import slick.driver.MySQLDriver.api._

import com.tripPlanner.shared.domain.User
import com.tripPlanner.domain.Tables.{User => Users, UserRow}
import scala.concurrent.duration._

/**
  * Created by rjkj on 12/9/15.
  */
trait UserDao {
  def update(user: User): Future[Long]

  def create(user: User): Future[String]
}

case class UserDaoImpl(db: Database)(implicit ec: ExecutionContext) extends UserDao {
  def update(user: User):Future[Long] = {
    val query = for {
      u <- Users if u.id === user.id
    } yield (u.firstName, u.lastName)

    val update = query.update(user.fName, user.lName)
    db.run(update) map {
      result => result
    }
  }

  def create(user: User):Future[String] = {
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val dateFromString: Date = user.registrationDate match {
      case Some(stringDate: String) => dateFormat.parse(stringDate)
      case None => new Date()
    }

    val userId = (Users returning Users.map(_.id)) += UserRow(user.id, user.fName, user.lName, new java.sql.Date(dateFromString.getTime))

    db.run(userId) map {
      result => result
    }
  }

}