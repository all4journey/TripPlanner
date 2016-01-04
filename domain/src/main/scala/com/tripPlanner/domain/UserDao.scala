package com.tripPlanner.domain


import java.text.SimpleDateFormat
import java.util.Date

import scala.concurrent.{Await, ExecutionContext, Future}

import slick.driver.MySQLDriver.api._

import com.tripPlanner.shared.domain.User
import com.tripPlanner.domain.Tables.{User=>Users, UserRow}
import scala.concurrent.duration._

/**
  * Created by rjkj on 12/9/15.
  */
trait UserDao {
  def save(user:User): Future[Long]
  def create(user: User): String
}

case class UserDaoImpl(db:Database)(implicit ec:ExecutionContext) extends UserDao {
  def save(user:User) = {
    storeInDb(user, createUser = false)
  }

  def create(user: User) = {
    storeInDb(user, createUser = true)
  }

  private def storeInDb(user:User, createUser: Boolean): Future[Long] = {
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val dateFromString:Date = user.registrationDate match {
      case Some(stringDate:String) => dateFormat.parse(stringDate)
      case None => new Date()
    }

    val primaryKey = if (createUser) java.util.UUID.randomUUID.toString else {
      ensureValidId(user.id)
      user.id
    }

    val insertUser = Users += UserRow(primaryKey, user.fName, user.lName, new java.sql.Date(dateFromString.getTime))

    db.run(insertUser) map {
      result => result
    }
  }

  private def ensureValidId(userId: String): Unit = {
//    val userListQuery = TableQuery[Users].filter(_.id === userId)
//    val userListFuture = db.run(userListQuery.result) map {
//      userListQuery => for {
//        u <- userListQuery
//      } yield User(u.id, u.firstName, u.lastName, None);
//    }
    val userCountQuery = TableQuery[Users].filter(_.id === userId).length
    val userCountFuture = db.run(userCountQuery.result)
    val myUserCount = Await.result(userCountFuture, 10 seconds)

    if (myUserCount != 1) {
      // need to make sure akka http doesn't return this exception to the browser
      throw new IllegalStateException("The user ID  is not valid")
    }
  }

}