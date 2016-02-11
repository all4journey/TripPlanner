package com.all4journey.domain

import java.text.SimpleDateFormat
import java.util.{UUID, Date}

import scala.concurrent.{Await, ExecutionContext, Future}

import slick.driver.MySQLDriver.api._

import com.all4journey.shared.domain.User
import com.all4journey.domain.Tables.{User => Users, UserRow}
import scala.concurrent.duration._

/**
  * Created by rjkj on 12/9/15.
  */
case class UserDao(db: Database)(implicit ec: ExecutionContext){
  /**
    * Updates a user's name
    * @param user User to be updated
    * @return 1 if successful
    */
  def update(user: User):Future[Long] = {
    val query = for {
      u <- Users if u.id === user.id
    } yield (u.firstName, u.lastName)

    val update = query.update(user.fName, user.lName)
    db.run(update) map {
      result => result
    }
  }

  def create(user: User):Future[Option[String]] = {
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val dateFromString: Date = user.registrationDate match {
      case Some(stringDate: String) => dateFormat.parse(stringDate)
      case None => new Date()
    }

    val userId = java.util.UUID.randomUUID().toString

    val result = Users += UserRow(userId, user.fName, user.lName, user.email, new java.sql.Date(dateFromString.getTime))

    db.run(result) map {
      result =>
        if(result == 1)
          Some(userId)
        else
          None
    }
  }

  def delete(user: User): Future[Boolean] = {
    val query = Users.filter(_.id === user.id)
    val action = query.delete

    db.run(action) map {
      case 1 => true
      case _ => false
    }

  }

  def getUserById(userId: String): Future[Seq[User]] = {
    val query = Users.filter(_.id === userId)
    val action = query.result

    db.run(action) map {
      userList => for {
        u <- userList
      } yield User(u.id, u.firstName, u.lastName, u.emailAddress, Some(u.registrationDate.toString))
    }
  }
}